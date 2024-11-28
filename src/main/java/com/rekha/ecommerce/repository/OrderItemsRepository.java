package com.rekha.ecommerce.repository;

import com.rekha.ecommerce.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItems, Long>, JpaSpecificationExecutor<OrderItems> {

    @Query(value = "select * from tb_order_items where order_id in :orderIds", nativeQuery = true)
    List<OrderItems> findByOrderId(List<Long> orderIds);
}
