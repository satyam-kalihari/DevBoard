package com.satyam.DevBoard.dto.response;

import com.satyam.DevBoard.model.Label;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabelResponse {
    private UUID id;
    private UUID orgId;
    private String orgName;
    private String name;
    private String colorHex;

    public static LabelResponse fromEntity(Label label){
        return LabelResponse.builder()
                .id(label.getId())
                .orgId(label.getOrganization().getId())
                .orgName(label.getOrganization().getName())
                .name(label.getName())
                .colorHex(label.getColorHex())
                .build();
    }
}
