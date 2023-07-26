package com.example.organisation.controller;

import com.example.organisation.dto.OrganisationDTO;
import com.example.organisation.dto.OrganisationDetail;
import com.example.organisation.service.OrganisationService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class OrganisationController {

  @Autowired private OrganisationService organisationService;

  @CircuitBreaker(name="org-details",fallbackMethod = "organizationDetailsFallback")
  @GetMapping("/org-details")
  public ResponseEntity<OrganisationDTO> organizationDetails() {
    log.info("Inside Organisation Controller's {} method","organizationDetails()");
    OrganisationDTO organisationDTO = new OrganisationDTO();
    organisationDTO.setOrganisationDetail(new OrganisationDetail());
    organisationDTO.setEmployeesDTOs(organisationService.getAllEmployees().getBody());
    return ResponseEntity.status(HttpStatus.OK).body(organisationDTO);
  }
  
  public ResponseEntity<OrganisationDTO> organizationDetailsFallback(Exception e) {
    log.info("Fallback is executed because student service is down: " + e.getMessage());
    OrganisationDTO organisationDTO = new OrganisationDTO();
    organisationDTO.setOrganisationDetail(new OrganisationDetail());
    return ResponseEntity.status(HttpStatus.OK).body(organisationDTO);
  }
}
