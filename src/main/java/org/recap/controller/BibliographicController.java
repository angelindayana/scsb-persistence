package org.recap.controller;

import org.recap.model.BibliographicEntity;
import org.recap.model.BibliographicPK;
import org.recap.repository.BibliographicDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pvsubrah on 6/11/16.
 */

@RestController
@RequestMapping("/bibliographic")
public class BibliographicController {
    private final BibliographicDetailsRepository bibliographicDetailsRepository;

    @Autowired
    public BibliographicController(BibliographicDetailsRepository bibliographicDetailsRepository) {
        this.bibliographicDetailsRepository = bibliographicDetailsRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @RequestMapping(method = RequestMethod.GET, value = "/findOne")
    public BibliographicEntity findOne(Integer owningInstitutionId, String owningInstitutionBibId) {
        BibliographicPK bibliographicPK = new BibliographicPK();
        bibliographicPK.setOwningInstitutionId(owningInstitutionId);
        bibliographicPK.setOwningInstitutionBibId(owningInstitutionBibId);
        return bibliographicDetailsRepository.findOne(bibliographicPK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/findAll")
    public List<BibliographicEntity> findAll(int pageNum, int numberOfRecords) {
        List<BibliographicEntity> bibliographicEntityList = new ArrayList<>();
        bibliographicEntityList.addAll(bibliographicDetailsRepository.findAll(new PageRequest(pageNum, numberOfRecords)).getContent());
        return bibliographicEntityList;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/count")
    public long count() {
        return bibliographicDetailsRepository.count();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    @Transactional
    public Integer create(@RequestBody BibliographicEntity bibliographicEntity) {
        BibliographicEntity savedBibliographicEntity = bibliographicDetailsRepository.saveAndFlush(bibliographicEntity);
        entityManager.refresh(savedBibliographicEntity);
        return savedBibliographicEntity.getBibliographicId();
    }
}
