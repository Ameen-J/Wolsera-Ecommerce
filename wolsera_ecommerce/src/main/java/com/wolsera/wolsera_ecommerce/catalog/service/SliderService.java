package com.wolsera.wolsera_ecommerce.catalog.service;

import com.wolsera.wolsera_ecommerce.catalog.dto.SliderRequestDTO;
import com.wolsera.wolsera_ecommerce.catalog.dto.SliderResponseDTO;
import com.wolsera.wolsera_ecommerce.catalog.model.Slider;
import com.wolsera.wolsera_ecommerce.catalog.repository.SliderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SliderService {

    @Autowired
    private SliderRepository sliderRepository;

    public List<SliderResponseDTO> getSliders() {
        return sliderRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public SliderResponseDTO createSlider(SliderRequestDTO dto) {
        Slider slider = new Slider();

        if (dto.getImageUrl() != null) slider.setImageUrl(dto.getImageUrl());
        if (dto.getTitle() != null) slider.setTitle(dto.getTitle());
        if (dto.getSubtitle() != null) slider.setSubtitle(dto.getSubtitle());
        if (dto.getButtonText() != null) slider.setButtonText(dto.getButtonText());
        if (dto.getLink() != null) slider.setLink(dto.getLink());

        return mapToDTO(sliderRepository.save(slider));
    }
    public SliderResponseDTO updateSlider(Long id, SliderRequestDTO dto) {

        Slider slider = sliderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slider not found with id: " + id));

        if (dto.getImageUrl() != null) slider.setImageUrl(dto.getImageUrl());
        if (dto.getTitle() != null) slider.setTitle(dto.getTitle());
        if (dto.getSubtitle() != null) slider.setSubtitle(dto.getSubtitle());
        if (dto.getButtonText() != null) slider.setButtonText(dto.getButtonText());
        if (dto.getLink() != null) slider.setLink(dto.getLink());

        Slider updated = sliderRepository.save(slider);

        return mapToDTO(updated); // your existing method
    }

    public void deleteSlider(Long id) {

        Slider slider = sliderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slider not found with id: " + id));

        sliderRepository.delete(slider);
    }

    private SliderResponseDTO mapToDTO(Slider slider) {
        SliderResponseDTO dto = new SliderResponseDTO();
        dto.setId(slider.getId());
        dto.setImageUrl(slider.getImageUrl());
        dto.setTitle(slider.getTitle());
        dto.setSubtitle(slider.getSubtitle());
        dto.setButtonText(slider.getButtonText());
        dto.setLink(slider.getLink());
        return dto;
    }
}