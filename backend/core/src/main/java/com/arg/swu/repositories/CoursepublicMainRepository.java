package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.CoursepublicMain;

import jakarta.transaction.Transactional;

/**
 * 
 * @author sitthichaim
 *
 */
public interface CoursepublicMainRepository extends JpaRepository<CoursepublicMain, Long> {
	
	@Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
    		delete from coursepublic_main
    		where coursepublic_id = :coursepublicId
    		""")
    public void deleteCoursepublicMainByCoursepublicId(Long coursepublicId);

	@Query(nativeQuery = true, value = """
			select cm.course_code
			from coursepublic_main cpm
			left join course_main cm on cpm.course_id = cm.course_id
			where cpm.coursepublic_id = :coursepublicId
			""")
	public String getCourseCode(Long coursepublicId);
	
	public CoursepublicMain findByCoursepublicId(Long coursepublicId);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = """
			update coursepublic_main cm
			set total_register = (select count(mc.member_course_id) from member_course mc where mc.coursepublic_id = cm.coursepublic_id and mc.payment_status=30017002)
			where cm.coursepublic_id = :coursepublicId
			""")
	public void updateTotalRegister(Long coursepublicId);
	
}
