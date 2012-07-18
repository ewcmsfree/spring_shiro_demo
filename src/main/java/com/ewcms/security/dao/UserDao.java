package com.ewcms.security.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.security.model.User;

public interface UserDao extends PagingAndSortingRepository<User, Long> {
	User findByLoginName(String loginName);
}
