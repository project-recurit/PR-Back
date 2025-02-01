package com.example.sideproject.domain.techstack;

import com.example.sideproject.domain.pr.entity.PublicRelation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class PrTechStackConnection extends AbstractTechStackConnection<PublicRelation> {
}
