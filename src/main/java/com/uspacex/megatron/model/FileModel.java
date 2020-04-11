package com.uspacex.megatron.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FileModel {
    private String name;
    private String type;
    @JsonIgnore
    private String path;
    private String fingerprint;
    private boolean dir;
    private List<FileModel> fileList;
}
