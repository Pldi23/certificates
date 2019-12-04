package com.epam.esm.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadFileResponse {

    private String fileName;
    private String fileDownloadUri;
    private String mimeType;
    private long size;

    public UploadFileResponse(String fileName, String fileDownloadUri, String mimeType, long size) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.mimeType = mimeType;
        this.size = size;
    }

}
