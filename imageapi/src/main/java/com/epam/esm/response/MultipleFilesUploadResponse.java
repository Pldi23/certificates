package com.epam.esm.response;

import lombok.Value;

import java.util.List;

@Value
public class MultipleFilesUploadResponse {

    private List<UploadFileResponse> uploadFileResponses;
    private List<ViolationResponse> violationResponses;
}
