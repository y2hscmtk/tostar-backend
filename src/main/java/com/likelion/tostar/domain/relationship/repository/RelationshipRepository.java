package com.likelion.tostar.domain.relationship.repository;

import com.likelion.tostar.domain.relationship.entity.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RelationshipRepository extends JpaRepository<Relationship, Long> {

}
