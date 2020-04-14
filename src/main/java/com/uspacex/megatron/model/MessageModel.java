package com.uspacex.megatron.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class MessageModel {
    private String id;
    private String content;
    private Date timeCreated;
}
