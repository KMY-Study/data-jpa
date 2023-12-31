package study.datajpa.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testEntity(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamA);
        Member member4 = new Member("member4", 11, teamB);
        Member member5 = new Member("member5", 12, teamB);
        Member member6 = new Member("member6", 13, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        em.persist(member5);
        em.persist(member6);

        em.flush(); // db insert query 실행
        em.clear();

        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

        for(Member m : members){
            System.out.println("member : " + m);
            System.out.println("  -> member.team : " + m.getTeam());

        }
    }

    @Test
    public void JpaEventBaseEntity() throws InterruptedException {
        //given
        Member member = new Member("member1");
        memberRepository.save(member); // @Prepersist

        Thread.sleep(100);
        member.setUsername("member2");

        em.flush(); //@PreUpdate
        em.clear();

        //when
        Member findMember = memberRepository.findById(member.getId()).get();

        //then
        System.out.println("findMember.getCreateDate ::: [" + findMember.getCreateDate() + "]");
//        System.out.println("findMember.getUpdateData ::: " + findMember.getUpdateDate());
        System.out.println("findMember.getLastModifiedDate ::: [" + findMember.getLastModifiedDate() + "]");
        System.out.println("findMember.getCreateBy ::: [" + findMember.getCreateBy() + "]");
        System.out.println("findMember.getLastModifedBy ::: [" + findMember.getLastModifiedBy() + "]");
    }
}