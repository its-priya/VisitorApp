package io.bootify.visitor_app.service;

import io.bootify.visitor_app.domain.Flat;
import io.bootify.visitor_app.model.FlatDTO;
import io.bootify.visitor_app.repos.FlatRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class FlatService {

    private final FlatRepository flatRepository;

    public FlatService(final FlatRepository flatRepository) {
        this.flatRepository = flatRepository;
    }

    public List<FlatDTO> findAll() {
        return flatRepository.findAll()
                .stream()
                .map(Flat -> mapToDTO(Flat, new FlatDTO()))
                .collect(Collectors.toList());
    }

    public FlatDTO get(final Long id) {
        return flatRepository.findById(id)
                .map(Flat -> mapToDTO(Flat, new FlatDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final FlatDTO FlatDTO) {
        final Flat Flat = new Flat();
        mapToEntity(FlatDTO, Flat);
        return flatRepository.save(Flat).getId();
    }

    public void update(final Long id, final FlatDTO FlatDTO) {
        final Flat Flat = flatRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(FlatDTO, Flat);
        flatRepository.save(Flat);
    }

    public void delete(final Long id) {
        flatRepository.deleteById(id);
    }

    private FlatDTO mapToDTO(final Flat flat, final FlatDTO flatDTO) {
        flatDTO.setId(flat.getId());
        flatDTO.setFlatNumber(flat.getFlatNumber());
        return flatDTO;
    }

    private Flat mapToEntity(final FlatDTO flatDTO, final Flat flat) {
        flat.setFlatNumber(flatDTO.getFlatNumber());
        return flat;
    }

}
