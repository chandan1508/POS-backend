package com.chandan.pos.repository;

import com.chandan.pos.domain.SubscriptionStatus;
import com.chandan.pos.modal.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    // All subscriptions for a store (no filter)
    List<Subscription> findByStoreId(Long storeId);

    // All subscriptions for a store filtered by status
    List<Subscription> findByStoreIdAndStatus(Long storeId, SubscriptionStatus status);

    // Active or trial subscription for a store
    Optional<Subscription> findTopByStoreIdAndStatusInOrderByCreatedAtDesc(
            Long storeId, List<SubscriptionStatus> statuses);

    // Admin: all with optional status filter
    List<Subscription> findByStatus(SubscriptionStatus status);

    // Subscriptions expiring within a date range
    List<Subscription> findByStatusAndEndDateBetween(
            SubscriptionStatus status, LocalDate from, LocalDate to);

    // Count grouped by status
    @Query("SELECT s.status, COUNT(s) FROM Subscription s GROUP BY s.status")
    List<Object[]> countGroupedByStatus();

    // Count by a specific status
    long countByStatus(SubscriptionStatus status);
}