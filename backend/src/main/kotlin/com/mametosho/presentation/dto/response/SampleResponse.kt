package com.mametosho.presentation.dto.response

import com.mametosho.domain.model.Sample

data class SampleResponse(
    val id: Long,
    val name: String
) {
    companion object {
        fun from(sample: Sample): SampleResponse {
            return SampleResponse(
                id = sample.id,
                name = sample.name
            )
        }
    }
}
