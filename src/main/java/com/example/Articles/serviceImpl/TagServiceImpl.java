package com.example.Articles.serviceImpl;

import com.example.Articles.entity.Tag;
import com.example.Articles.repository.TagRepository;
import com.example.Articles.service.TagService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public Tag getTagById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Тег не найден"));
    }

    @Override
    public void createTag(Tag tag) {
        tagRepository.save(tag);
    }
    @Override
    public Tag updateTag(Long id, Tag tagDetails) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Тег не найден"));
        tag.setName(tagDetails.getName());
        return tagRepository.save(tag);
    }
    @Override
    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new IllegalArgumentException("Тег не найден");
        }
        tagRepository.deleteById(id);
    }
}
