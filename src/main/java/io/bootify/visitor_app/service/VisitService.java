package io.bootify.visitor_app.service;

import io.bootify.visitor_app.domain.Flat;
import io.bootify.visitor_app.domain.Visit;
import io.bootify.visitor_app.domain.Visitor;
import io.bootify.visitor_app.model.VisitDTO;
import io.bootify.visitor_app.model.VisitStatus;
import io.bootify.visitor_app.repos.FlatRepository;
import io.bootify.visitor_app.repos.VisitRepository;
import io.bootify.visitor_app.repos.VisitorRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final VisitorRepository visitorRepository;

    private final FlatRepository flatRepository;

    public VisitService(final VisitRepository visitRepository,
                        final VisitorRepository visitorRepository, FlatRepository flatRepository) {
        this.visitRepository = visitRepository;
        this.visitorRepository = visitorRepository;
        this.flatRepository = flatRepository;
    }

    public List<VisitDTO> findAll() {
        return visitRepository.findAll()
                .stream()
                .map(visit -> mapToDTO(visit, new VisitDTO()))
                .collect(Collectors.toList());
    }

    public VisitDTO get(final Long id) {
        return visitRepository.findById(id)
                .map(visit -> mapToDTO(visit, new VisitDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<VisitDTO> getAllApprovedVisits(){
        return visitRepository.findAll()
                .stream()
                .filter(visit -> visit.getStatus().equals(VisitStatus.APPROVED))
                .map(visit -> mapToDTO(visit, new VisitDTO()))
                .collect(Collectors.toList());
    }

    public Long create(final VisitDTO visitDTO) {
        final Visit visit = new Visit();
        mapToEntity(visitDTO, visit);
        return visitRepository.save(visit).getId();
    }

    public void markEntry(Long visitId){
        Visit visit= visitRepository.findById(visitId).get();
        if(visit!=null && visit.getStatus().equals(VisitStatus.APPROVED)) {
            visit.setEntryTime(LocalDateTime.now());
            visitRepository.save(visit);
        }
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status not updated.");
    }

    public void markExit(Long visitId){
        Visit visit= visitRepository.findById(visitId).get();
        if(visit!=null && visit.getStatus().equals(VisitStatus.APPROVED)) {
            visit.setExitTime(LocalDateTime.now());
            visit.setStatus(VisitStatus.COMPLETED);
            visitRepository.save(visit);
        }
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status not updated.");
    }

    public void update(final Long id, final VisitDTO visitDTO) {
        final Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(visitDTO, visit);
        visitRepository.save(visit);
    }

    public void delete(final Long id) {
        visitRepository.deleteById(id);
    }

    private VisitDTO mapToDTO(final Visit visit, final VisitDTO visitDTO) {
        visitDTO.setId(visit.getId());
        visitDTO.setStatus(visit.getStatus());
        visitDTO.setEntryTime(visit.getEntryTime());
        visitDTO.setExitTime(visit.getExitTime());
        visitDTO.setImageUrl(visit.getImageUrl());
        visitDTO.setPurpose(visit.getPurpose());
        visitDTO.setTotalVisitors(visit.getTotalVisitors());
        visitDTO.setVisitorId(visit.getVisitor() == null ? null : visit.getVisitor().getId());
        visitDTO.setFlatId(visit.getFlat().getId());
        return visitDTO;
    }

    private Visit mapToEntity(final VisitDTO visitDTO, final Visit visit) {
        visit.setStatus(visitDTO.getStatus());
        visit.setEntryTime(visitDTO.getEntryTime());
        visit.setExitTime(visitDTO.getExitTime());
        visit.setImageUrl(visitDTO.getImageUrl());
        visit.setPurpose(visitDTO.getPurpose());
        visit.setTotalVisitors(visitDTO.getTotalVisitors());
        Flat flat= flatRepository.findById(visitDTO.getFlatId()).get();
        visit.setFlat(flat);
        if (visitDTO.getVisitorId() != null && (visit.getVisitor() == null || !visit.getVisitor().getId().equals(visitDTO.getVisitorId()))) {
            final Visitor visitor = visitorRepository.findById(visitDTO.getVisitorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Visitor not found"));
            visit.setVisitor(visitor);
        }
        return visit;
    }

}
