package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

import javax.swing.text.html.parser.Entity;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipants() {
		Collection<Participant> participants = participantService.getAll();
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {
		if (participantService.findByLogin(participant.getLogin()) != null) {
			return new ResponseEntity("Unable to create. A participant with login " + participant.getLogin() + " already exist.", HttpStatus.CONFLICT);
		}
		participantService.addParticipant(participant);
		return new ResponseEntity<Participant>(participant,HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant != null) {
			participantService.deleteParticipant(participant);
			return new ResponseEntity("Participant deleted " + participant.getLogin() , HttpStatus.OK);
		}
			return new ResponseEntity("Participant not found",HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Participant> updateParticipant(@PathVariable("id") String login, @RequestBody Participant participant) {
		if (participantService.findByLogin(login) != null) {
			participantService.updateParticipant(login, participant);
			return new ResponseEntity("Participant with login " + login + " updated.", HttpStatus.OK);
		}

		return new ResponseEntity("Participant with this login does not exist",HttpStatus.NOT_FOUND);
	}




}
