package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.MasBanner;

import java.util.List;

public interface MasBannerRepository extends JpaRepository<MasBanner, Long> {

    @Modifying
    @Query(nativeQuery = true, value = """
        select mb.*
        from mas_banner mb
        where (case when mb.display_start_date is null then true else (now() between mb.display_start_date and mb.display_end_date) end) = true 
        and mb.active_flag =true
        order by mb.order_by asc
    		""")
    public List<MasBanner> findBannerActive();
}
