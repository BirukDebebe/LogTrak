package logtrak_crm.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import logtrak_crm.model.Item;

public interface ItemRepository extends JpaRepository<Item, Integer>{
	List<Item> findByFullname(String fullname);

}
