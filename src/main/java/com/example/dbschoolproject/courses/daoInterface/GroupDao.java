package com.example.dbschoolproject.courses.daoInterface;

import com.example.dbschoolproject.courses.domain.Group;

import java.util.List;

public interface GroupDao {
    void saveAll(List<Group> groups);
    List<Group> findByGroupId(int count);
    List<Group> findAll();
}
