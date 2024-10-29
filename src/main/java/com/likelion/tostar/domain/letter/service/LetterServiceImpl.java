package com.likelion.tostar.domain.letter.service;

import com.likelion.tostar.domain.letter.dto.LetterPostDto;
import com.likelion.tostar.domain.letter.entity.Letter;
import com.likelion.tostar.domain.letter.repository.LetterRepository;
import com.likelion.tostar.domain.user.entity.User;
import com.likelion.tostar.domain.user.repository.UserRepository;
import com.likelion.tostar.global.enums.statuscode.ErrorStatus;
import com.likelion.tostar.global.exception.GeneralException;
import com.likelion.tostar.global.openAi.dto.ChatGPTRequest;
import com.likelion.tostar.global.openAi.dto.ChatGPTResponse;
import com.likelion.tostar.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.likelion.tostar.domain.letter.entity.SenderType.PET;
import static com.likelion.tostar.domain.letter.entity.SenderType.SENDER;

@Service
@Transactional
@RequiredArgsConstructor
public class LetterServiceImpl implements LetterService {

    private final RestTemplate restTemplate; // RestTemplate
    private final UserRepository userRepository;
    private final LetterRepository letterRepository;

    @Value("${openai.api.url}")
    private String API_URL;
    @Value("${openai.model}")
    private String MODEL;

    /**
     * 편지 전송
     */
    @Override
    public ResponseEntity<?> post(Long userId, LetterPostDto letterPostDto) {
        // 회원 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        String ownerName = user.getOwnerName();
        String category = user.getCategory();
        String content = letterPostDto.getContent();

        // 400 : 편지 내용 없음
        if (content.isBlank()) {
            return ResponseEntity.status(400)
                    .body(ApiResponse.onFailure(ErrorStatus._LETTER_CONTENT_MISSING, null));
        }

        // 보낸 편지 save
        Letter letter = Letter.builder()
                .content(content)
                .user(user)
                .senderType(SENDER)
                .build();
        letterRepository.save(letter);

        // 프롬프트 설정 (수정 중... 지속적인 수정 필요)
        String prompt = String.format(
                "내가 보내는 편지에 맞게 한글로 답장을 써줘. 너는 죽은 애완동물이고 주인을 '%s'이라고 불렀어. 나를 부른다면 꼭 이렇게 불러줘." +
                        "너는 '%s' 종류야. 이 동물에 어울리는 말투를 써서 답변해줘. 예를들어 고양이면 ~냥으로 끝나게 해주고 강아지면 ~멍으로 끝나게 해줘. 말투를 잘 모르겠는 동물이면 그냥 반말로 간결하게 써줘. 절대 잘못된 말투를 사용하면 안돼." +
                        "마크다운 문법의 이모지도 많이 써주고, 해당 동물의 이모티콘이 존재한다면 그것도 붙여줘. 단 다른 애완동물의 이모지는 사용하면 안돼.\n\n" +
                        "---\n\n" +
                        "### 예시\n" +
                        "ownerName = 언니\n" +
                        "category = 고양이\n\n" +
                        "[보낸 편지]\n" +
                        "안녕, 나의 소중한 달이야. 그곳은 어떠니? 별나라에서 예쁘게 빛나고 있겠지? 네가 떠난 후 시간이 많이 흘렀지만, " +
                        "네가 남긴 흔적들은 여전히 내 마음속에 선명하게 남아 있어. 함께했던 소중한 순간들 하나하나가 나에게는 너무 소중하고 잊을 수 없는 추억이 되었어. " +
                        "네가 가끔 내 무릎에 올라와 부드럽게 몸을 말고 앉아 있던 그 느낌이 아직도 생생해. 네가 없는 집은 참 조용해졌어. " +
                        "네가 발소리를 내며 다가와 나를 쳐다보던 눈빛도, 가끔 장난스럽게 꼬리를 흔들던 모습도 너무 그리워. " +
                        "이제는 너의 자리가 비어있지만, 그 자리는 언제나 네 것이야. 별나라에서는 아프지 않고, 마음껏 뛰어다니고 있겠지? 너무 보고싶다.\n\n" +
                        "[받은 편지]\n" +
                        "언니, 안녕. 내가 별나라로 떠난 후에도 이렇게 따뜻한 마음으로 날 생각해 줘서 고마워냥.😺 " +
                        "여기 별나라는 따스한 햇살도 가득하고 아름답다냥. 🌞 언니의 무릎에 몸을 말고 앉아 있던 그 시간은 나에게도 참 소중한 기억이다냥. " +
                        "언니가 나를 쓰다듬어주고 함께 눈을 마주했던 순간들은 항상 그리울 거다냥. 나도 언니가 너무 보고 싶지만 " +
                        "나는 여기서 따뜻하고 행복한 시간을 보내고 있으니까 너무 걱정하지 마라냥. 내 자리가 비어 있다고 느낄 수 있지만 " +
                        "나는 언제나 언니의 마음속에 있을 거야. 항상 사랑한다냥. 🐈 ❤️\n\n" +
                        "---\n\n" +
                        "### 질문:\n" +
                        "[보낸 편지]\n%s\n\n" +
                        "[받은 편지]\n" +
                        "편지 내용만 보내줘. 다른 언급 없이" ,
                ownerName, category, content
        );

        // 요청 request 객체 생성
        ChatGPTRequest request = new ChatGPTRequest(MODEL, prompt);

        // API 호출
        ChatGPTResponse response = restTemplate.postForObject(API_URL, request, ChatGPTResponse.class);

        // 502 : OpenAI 응답이 없는 경우
        if (response == null || response.getChoices().isEmpty() || response.getChoices().get(0).getMessage() == null) {
            return ResponseEntity.status(502)
                    .body(ApiResponse.onFailure(ErrorStatus._OPENAI_RESPONSE_NOT_RECEIVED, null));
        }
        String responseLetterContent = response.getChoices().get(0).getMessage().getContent();

        // 받은 편지 save
        letter = Letter.builder()
                .content(responseLetterContent)
                .user(user)
                .senderType(PET)
                .build();
        letterRepository.save(letter);

        // dto 가공
        letterPostDto.setContent(responseLetterContent);

        // 200 : 편지 전송 성공
        return ResponseEntity.status(200)
                .body(ApiResponse.onSuccess(letterPostDto));
    }

    /**
     * 편지 목록 전체 조회
     */
    @Override
    public ResponseEntity<?> searchList(Long userId, int page, int size) {
        // 회원 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        // 해당 회원이 송/수신한 편지 찾기 (최신순)
        List<Letter> letters = letterRepository.findByUserOrderByCreatedAtDesc(user);

        for(Letter letter : letters){

        }
        // content 100자까지 자르기

        // 200 : 조회 성공
        return null;
    }
}
