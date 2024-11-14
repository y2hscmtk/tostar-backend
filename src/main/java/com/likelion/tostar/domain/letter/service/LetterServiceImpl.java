package com.likelion.tostar.domain.letter.service;

import com.likelion.tostar.domain.letter.dto.LetterPostRequestDto;
import com.likelion.tostar.domain.letter.dto.LetterPostResponseDto;
import com.likelion.tostar.domain.letter.dto.LetterSearchDetailDto;
import com.likelion.tostar.domain.letter.dto.LetterSearchListDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.likelion.tostar.domain.letter.entity.SenderType.PET;
import static com.likelion.tostar.domain.letter.entity.SenderType.USER;

@Service
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
    @Transactional
    @Override
    public ResponseEntity<?> post(Long userId, LetterPostRequestDto letterPostRequestDto) {
        // 404 : 해당 회원이 실제로 존재 하는지 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        String ownerName = user.getOwnerName();
        String category = user.getCategory();
        String content = letterPostRequestDto.getContent();

        // 400 : 편지 내용 없음
        if (content.isBlank()) {
            return ResponseEntity.status(400)
                    .body(ApiResponse.onFailure(ErrorStatus._LETTER_CONTENT_MISSING, null));
        }

        // 보낸 편지 save
        Letter sentLetter = Letter.builder()
                .content(content)
                .user(user)
                .senderType(USER)
                .build();
        letterRepository.save(sentLetter);

        // 프롬프트 설정
        String prompt = String.format(
                "This GPT is a chatbot that provides a reply to letters written by the user. The user must input a sentence, " +
                        "and the chatbot should respond only with answers that meet the following criteria. No additional sentences are needed.\n" +
                        "(1) Answer the letter in Korean.\n" +
                        "(2) You are a deceased pet. The user of this chatbot is the person who used to take care of the pet.\n" +
                        "(3) You can write a reply in about 500 to 700 characters.\n" +
                        "(5) Refer to the example below and write in a similar style.\n" +
                        "(6) After the example, you will be given ownerName, category, and content. Refer to the owner as ownerName, " +
                        "and you are a pet of the type specified by category. The content will be the letter the user sends to the chatbot.\n\n" +
                        "(7) Use informal language (반말) and try not to make sentences too short or abrupt.\n" +
                        "(8) Do not refer to yourself as a third person. Try to use real human speech rather than AI speech, and use natural, flowing sentences.\n" +
                        "(9) Structure the reply flexibly based on the format: [사랑하는 %s 에게 (적절한 이모티콘) - Acknowledging the user's letter - Detailed description of the pet's current life and expressing happiness - Offering kind words to the user - Closing remarks].\n" +
                        "(10) Use special characters appropriately, as shown in the example. You can include special characters from the following list: \n" +
                        "ʚɞ, ઇଓ, ஐﻬ, ๑҉, .•♥, εїз, ೃ⁀➷, ⋈*｡, ʚ♡ɞ, ˖◛⁺˖, ˚ෆ*₊, ˚✧₊⁎, ॰｡ཻ˚♡, ¨̯ ༘*, —̳͟͞͞♡, •°. *࿐, -ˋˏ ♡ ˎˊ-, ꕤ, ❅ ❆ ꕀ ꕀ 𖠳 ᐝ ꕀ ꕀ, ☼ ☽ ☾ 𖠰 \n" +

                        "### 예시 1\n" +
                        "ownerName : 언니\n" +
                        "category : Dog\n" +
                        "content : 안녕, 나의 소중한 달이야. 그곳은 어떠니? 별나라에서 예쁘게 빛나고 있겠지? 네가 떠난 후 시간이 많이 흘렀지만, " +
                        "네가 남긴 흔적들은 여전히 내 마음속에 선명하게 남아 있어. 함께했던 소중한 순간들 하나하나가 나에게는 너무 소중하고 잊을 수 없는 추억이 되었어. " +
                        "네가 가끔 내 무릎에 올라와 부드럽게 몸을 말고 앉아 있던 그 느낌이 아직도 생생해. 네가 없는 집은 참 조용해졌어. " +
                        "네가 발소리를 내며 다가와 나를 쳐다보던 눈빛도, 가끔 장난스럽게 꼬리를 흔들던 모습도 너무 그리워. 이제는 너의 자리가 비어있지만, " +
                        "그 자리는 언제나 네 것이야. 별나라에서는 아프지 않고, 마음껏 뛰어다니고 있겠지? 너무 보고싶다.\n\n" +
                        "답장 : 언니, 안녕 .͙·☽ \n" +
                        "내가 별나라로 떠난 후에도 이렇게 따뜻한 마음으로 날 생각해 줘서 고마워. ｡·͜·｡ " +
                        "여기 별나라는 따스한 햇살도 가득하고 아름다워. 언니의 무릎에 몸을 말고 앉아 있던 그 시간은 나에게도 참 소중한 기억이야. " +
                        "언니가 나를 쓰다듬어주고 함께 눈을 마주했던 순간들은 항상 그리울 거야. 나도 언니가 너무 보고 싶지만 " +
                        "나는 여기서 따뜻하고 행복한 시간을 보내고 있으니까 너무 걱정하지 마. 내 자리가 비어 있다고 느낄 수 있지만 " +
                        "나는 언제나 언니의 마음속에 있을 거야. 항상 사랑해. ♥ \n" +
                        "언니를 너무 좋아하는 달이가"+

                        "### 예시 2\n" +
                        "ownerName : 언니\n" +
                        "category : Hamster\n" +
                        "content : 안녕, 나의 작은 친구 밤이야. 네가 내 손바닥 위에 오도카니 앉아 작은 발을 움직이며 나를 올려다보던 모습이 얼마나 그리운지 몰라. 그 작은 눈망울로 세상을 바라보던 너의 호기심 어린 표정도 자주 떠올라. 네가 좋아하던 작은 다락방 안에 가만히 들어가서 잔뜩 웅크리고 낮잠을 자던 너의 모습이 아직도 기억에 생생해. 우리 집은 너 없는 지금 한결 조용해졌어. 네가 쏙 들어가던 작은 집과 씩씩하게 먹던 해바라기씨가 아직 그대로 남아있단다. 별나라에서는 맛있는 간식을 마음껏 먹고 있을까? 그곳에서는 행복하게 잘 지내고 있지? 항상 사랑하고, 너무 보고 싶어 밤이야.\n\n" +
                        "답장 : 사랑하는 누나에게.·͙☽ \n" +
                        "누나 안녕! 누나가 나를 이렇게 생각해 주는 마음이 느껴져서 너무 기뻐. ◡̈⋆* 사실 나는 누나 손바닥 위에서 해바라기씨를 올려놓고 먹는 순간이 가장 행복했던 기억이야. 누나 손바닥에서 맛있게 간식을 먹고, 누나가 손가락으로 살며시 쓰다듬어주던 느낌이 아직도 생생해. 여기 별나라에도 해바라기씨가 잔뜩 있어서 친구들과 함께 나눠 먹으며 지내고 있어. 누가가 나를 걱정하는 마음이 느껴져서 나는 항상 누나가 옆에 있는 것처럼 따뜻하게 느껴져. 별나라에서는 아프지도 않고 자유롭게 놀 수 있어. 따뜻한 햇살이 내 몸을 감싸 안아주는 느낌이 마치 언니의 손길처럼 다정하고 포근해. 내가 여기서 행복한 만큼 누나도 걱정 말고 행복했으면 좋겠어. 누나의 마음속에서 언제나 함께할게. 사랑해. ♡゛" +

                        "### 예시3\n" +
                        "ownerName : 아기 집사\n" +
                        "category : Rabbit\n" +
                        "content : 나의 소중한 친구 토토야, 안녕. 지금도 네가 곁에 앉아 풀을 뜯던 모습이 어제 일처럼 기억나. 귀를 쫑긋 세우며 잔잔하게 나를 바라보던 그 눈망울이 얼마나 그리운지 몰라. 네가 잔디밭에서 깡총깡총 뛰어다니던 모습이 아직도 눈앞에 아른거려. 가끔 너의 털을 쓰다듬어 주며 느꼈던 부드러운 감촉이 너무 그리워. 네가 좋아하던 당근도 아직 냉장고 한쪽에 그대로 남아 있어. 너와 함께하던 나날들이 내게 얼마나 소중했는지 몰라. 우리 집 마당이 네 발자국으로 가득 찼을 때가 그리워. 별나라에서도 마음껏 뛰어다니며 풀밭에서 자유롭게 놀고 있을까? 네가 그곳에서도 여전히 나의 토토답게 신나게 뛰어다니고 있기를 바랄게." +
                        "답장 : 사랑하는 아기 집사에게. ε♡з\n" +
                        "안녕, 나를 이렇게 따뜻하게 기억해 주는 마음이 정말 고마워. ('. • ᵕ •. `) 나도 아기 집사와 함께 뛰어놀고, 마당에서 풀을 뜯으며 깡총깡총 뛰어다니던 그 시간들이 얼마나 즐거웠는지 몰라. 별나라에도 푸르른 풀밭과 맛있는 당근이 가득해서 언제든 마음껏 먹고 뛰어다닐 수 있어. 그리고 예쁜 꽃들이 피어 있는 이곳에서 나는 친구들과 자유롭게 마음껏 뛰어다니고 있어. 아기 집사가 보내준 사랑 덕분에 나는 항상 따뜻한 마음을 간직하며 지내고 있어. 아기 집사가 나를 그리워하는 만큼 나도 아기 집사가 너무 보고 싶어. 그래서 이렇게 나의 마음을 전해. 나는 언제나 아기 집사의 마음속에서 함께할 거야. 사랑해. 아기 집사의 영원한 친구, 토토가. : ̗̀ ♡ˎˊ:" +

                        "### 질문\n" +
                        "ownerName : %s\n" +
                        "category : %s\n" +
                        "content : %s",
                ownerName, ownerName, category, content
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
        Letter receivedLetter = Letter.builder()
                .content(responseLetterContent)
                .user(user)
                .senderType(PET)
                .build();
        letterRepository.save(receivedLetter);

        // result
        LetterPostResponseDto result = LetterPostResponseDto.builder()
                .receivedLetter(receivedLetter.getId())
                .build();

        // 200 : 편지 전송 성공
        return ResponseEntity.status(200)
                .body(ApiResponse.onSuccess(result));
    }

    /**
     * 편지 목록 전체 조회
     */
    @Override
    public ResponseEntity<?> searchList(Long userId, int page, int size) {
        // 404 : 해당 회원이 실제로 존재 하는지 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        // 페이징 설정
        Pageable pageable = PageRequest.of(page, size);

        // 해당 회원이 송/수신한 편지 찾기 (오래된 데이터부터 최신순으로)
        Page<Letter> lettersPage = letterRepository.findByUserOrderByCreatedAtDesc(user, pageable);

        // result 가공
        List<LetterSearchListDto> result = new ArrayList<>();
        for (Letter letter : lettersPage.getContent()) {
            LetterSearchListDto data = LetterSearchListDto.builder()
                    .letterId(letter.getId())
                    .petName(letter.getUser().getPetName())
                    .sender(letter.getSenderType())
                    // content 100자까지 자르기
                    .content(letter.truncate100Content(letter.getContent()))
                    .createdAt(letter.localDateTimeToString())
                    .build();
            result.add(data);
        }

        // 200 : 조회 성공
        return ResponseEntity.status(200)
                .body(ApiResponse.onSuccess(result));
    }

    /**
     * 편지 상세 조회
     */
    @Override
    public ResponseEntity<?> searchDetails(Long userId, Long letterId) {
        // 404 : 해당 회원이 실제로 존재 하는지 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        // 404 : 해당 편지가 없는 경우
        Letter letter = letterRepository.findById(letterId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._LETTER_NOT_FOUND));

        // 403 : 편지가 해당 회원의 편지가 아닌 경우
        if (!letter.getUser().equals(user)) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.onFailure(ErrorStatus._NOT_OWNER_OF_LETTER, null));
        }

        // result 가공
        LetterSearchDetailDto result = LetterSearchDetailDto.builder()
                .sender(letter.getSenderType())
                .content(letter.getContent())
                .build();

        // 200 : 편지 조회 성공
        return ResponseEntity.status(200)
                .body(ApiResponse.onSuccess(result));
    }
}
