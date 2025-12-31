package com.mametosho.admin.presentation.controller

import com.mametosho.admin.application.usecase.FindSampleDetailUsecase
import com.mametosho.admin.presentation.dto.response.SampleResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/samples")
class SampleController(
    private val findSampleDetailUsecase: FindSampleDetailUsecase
) {
    @GetMapping("/{id}")
    fun getSampleById(@PathVariable id: Long): ResponseEntity<SampleResponse> {
        val sample = findSampleDetailUsecase.execute(id)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(SampleResponse.from(sample))
    }
}
