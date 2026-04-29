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

        slider.setImageUrl(dto.getImageUrl());
        slider.setTitle(dto.getTitle());
        slider.setSubtitle(dto.getSubtitle());
        slider.setButtonText(dto.getButtonText());
        slider.setLink(dto.getLink());

        return mapToDTO(sliderRepository.save(slider));
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