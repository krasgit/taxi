package com.matin.taxi.db;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface SimulateRepository extends Repository<Simulate, Integer> {

	@Query("SELECT ptype FROM PetType ptype ORDER BY ptype.name")
	@Transactional(readOnly = true)
	List<Simulate> findPetTypes();

	@Query("SELECT DISTINCT owner FROM Owner owner left join  owner.pets WHERE owner.lastName LIKE :lastName% ")
	@Transactional(readOnly = true)
	Page<Simulate> findByLastName(@Param("lastName") String lastName, Pageable pageable);


	@Query("SELECT owner FROM Owner owner left join fetch owner.pets WHERE owner.id =:id")
	@Transactional(readOnly = true)
	Simulate findById(@Param("id") Integer id);

	
	
	@Query("SELECT p FROM Principal p WHERE p.id =:id")
	@Transactional(readOnly = true)
	Principal getPrincipalById(@Param("id") Integer id);

	
	void save(Simulate simulate);
	void save(Principal principal);

	void save(Session session);

	void save(SessionAttributes sessionAttributes); 
	

    
	@Modifying
    @Query("DELETE SessionAttributes c WHERE c.id = ?1")
    void deleteByCustomerId(int customerId);

	
	
	@Query("SELECT owner FROM Owner owner")
	@Transactional(readOnly = true)
	Page<Simulate> findAll(Pageable pageable);

}
