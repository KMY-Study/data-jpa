package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
//public interface MemberRepository extends Repository<Member, Long> {
    //JpaRepository<Entity, PK의 type>

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age); //메소드 이름을 분석해서 JPQL을 생성하고 실행

//    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String name); //Collection return

    //실행할 메서드에 정적 쿼리를 직접 작성하므로 이름 없는 Named 쿼리라 할 수 있음

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m") // 단순히 값 하나를 조회
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name)" + "from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username = :name") // 위치 기반
//    @Query("select m from Member m where m.username = ?0") // 이름 기반
//    Member findMembers(@Param("name") String username); // 단건 return
    Optional<Member> findMembers(@Param("name") String username); // optional return

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

/*
    Page<Member> findByUsername(String name, Pageable pageable); // count 쿼리 사용 count쿼리 결과를 포함하는 페이징, count 쿼리 사용
    Slice<Member> findByUsername(String name, Pageable pageable); // count 쿼리 사용 안함
    List<Member> findByUsername(String name, Pageable pageable); // count 쿼리 사용 안함
    List<Member> findByUsername(String, Sort sort); // 정렬기능
*/
    Page<Member> findByAge(int age, Pageable pageable);

}
