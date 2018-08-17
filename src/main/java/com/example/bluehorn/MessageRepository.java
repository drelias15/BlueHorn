package com.example.bluehorn;

import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {
    Message findBySentBy(String sentby);
}

