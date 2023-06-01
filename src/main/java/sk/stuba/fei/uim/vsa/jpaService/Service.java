package sk.stuba.fei.uim.vsa.jpaService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import sk.stuba.fei.uim.vsa.bonus.Page;
import sk.stuba.fei.uim.vsa.bonus.Page1;
import sk.stuba.fei.uim.vsa.bonus.Pageable;
import sk.stuba.fei.uim.vsa.bonus.Pageable1;
import sk.stuba.fei.uim.vsa.bonus.PageableThesisService;
import sk.stuba.fei.uim.vsa.entities.Pedagog;
import sk.stuba.fei.uim.vsa.entities.Status;
import sk.stuba.fei.uim.vsa.entities.Student;
import sk.stuba.fei.uim.vsa.entities.Typ;
import sk.stuba.fei.uim.vsa.entities.ZaverecnaPraca;

public class Service extends AbstractThesisService<Student, Pedagog, ZaverecnaPraca> implements PageableThesisService<Student, Pedagog, ZaverecnaPraca> {

    private static Service instance;
    
    public static Service getInstance(){
        if(instance == null){
            instance = new Service();
        }
        return instance;
    }
    
    private Service() {
        super();
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public Student createStudent(Long aisId, String name, String email, String password) {
        if(this.getTeacherByEmail(email).isPresent()){
            throw new IllegalArgumentException();
        }
        Student s = new Student();

        EntityManager em = emf.createEntityManager();

        s.setAisId(aisId);
        s.setName(name);
        s.setEmail(email);
        s.setPassword(password);

        em.getTransaction().begin();
        try {
            em.persist(s);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return null;
        } finally {
            em.close();
        }

        return s;
    }

    public Optional<Student> getStudentByEmail(String email){
        EntityManager em = emf.createEntityManager();

        Optional<Student> s;

        TypedQuery<Student> s1 = em.createQuery("select s from Student s where s.email = :email", Student.class);
        s1.setParameter("email", email); 
        
        Optional<Student> sop = s1.getResultStream().findFirst();
        em.close();
        return sop;
    }
    
    public Student save(Student student){
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(student);
            em.getTransaction().commit();
            return student;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return null;
        } finally {
            em.close();
        }
    }
    
    @Override
    public Student getStudent(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        EntityManager em = emf.createEntityManager();

        Student s;

        em.getTransaction().begin();
        try {
            TypedQuery<Student> s1 = em.createQuery("select s from Student s where s.aisId = :aisId", Student.class);
            s1.setParameter("aisId", id);
            s = s1.getSingleResult();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            s = null;
        } finally {
            em.close();
        }
        return s;
    }

    @Override
    public Student updateStudent(Student student) {
        if (student == null || student.getAisId() == null) {
            throw new IllegalArgumentException();
        }
        Student s = this.getStudent(student.getAisId());

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        try {
            if (s != null) {
                s.setEmail(student.getEmail());
                s.setName(student.getName());
                s.setThesis(student.getThesis());
                s.setProgramme(student.getProgramme());
                s.setYear(student.getYear());
                s.setTerm(student.getTerm());

                s = em.merge(s);
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return null;
        } finally {
            em.close();
        }
        return s;
    }

    @Override
    public List<Student> getStudents() {
        EntityManager em = emf.createEntityManager();

        List<Student> sl = new ArrayList<>();

        try {
            TypedQuery<Student> s1 = em.createQuery("select s from Student s", Student.class);
            sl = s1.getResultList();
        } catch (Exception e) {
        } finally {
            em.close();
        }
        return sl;
    }

    @Override
    public Student deleteStudent(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        EntityManager em = emf.createEntityManager();

        Student s;

        em.getTransaction().begin();
        try {
            TypedQuery<Student> s1 = em.createQuery("select s from Student s where s.aisId = :aisId", Student.class);
            s1.setParameter("aisId", id);
            s = s1.getSingleResult();

            TypedQuery<ZaverecnaPraca> zp1 = em.createQuery("select z from ZaverecnaPraca z where z.author.aisId = :aisId", ZaverecnaPraca.class);
            zp1.setParameter("aisId", s.getAisId());
            List<ZaverecnaPraca> zpl = zp1.getResultList();
            for (ZaverecnaPraca zp : zpl) {
                if (zp.getStatus().equals(Status.IN_PROGRESS)) {
                    zp.setStatus(Status.FREE_TO_TAKE);
                }
                zp.setAuthor(null);
                em.merge(zp);
            }
            em.remove(s);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return null;
        } finally {
            em.close();
        }
        return s;
    }

    @Override
    public Pedagog createTeacher(Long aisId, String name, String email, String department, String password) {
        if(this.getStudentByEmail(email).isPresent()){
            throw new IllegalArgumentException();
        }
        Pedagog p = new Pedagog();
        p.setAisId(aisId);
        p.setName(name);
        p.setEmail(email);
        p.setPassword(password);
        p.setDepartment(department);
        p.setInstitute(department);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(p);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return null;
        } finally {
            em.close();
        }
        return p;
    }

    public Optional<Pedagog> getTeacherByEmail(String email){
        EntityManager em = emf.createEntityManager();

        Optional<Pedagog> p;

        TypedQuery<Pedagog> p1 = em.createQuery("select p from Pedagog p where p.email = :email", Pedagog.class);
        p1.setParameter("email", email);
        
        Optional<Pedagog> pop = p1.getResultStream().findFirst();
        em.close();
        return pop;
    }
    
    @Override
    public Pedagog getTeacher(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        EntityManager em = emf.createEntityManager();

        Pedagog p = new Pedagog();

        try {
            TypedQuery<Pedagog> p1 = em.createQuery("select p from Pedagog p where p.aisId = :aisId", Pedagog.class);
            p1.setParameter("aisId", id);
            p = p1.getSingleResult();
        } catch (Exception e) {
            p = null;
        } finally {
            em.close();
        }
        return p;
    }

    @Override
    public Pedagog updateTeacher(Pedagog teacher) {
        if (teacher == null || teacher.getAisId() == null) {
            throw new IllegalArgumentException();
        }
        Pedagog p = this.getTeacher(teacher.getAisId());

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        try {
            if (p != null) {
                p.setEmail(teacher.getEmail());
                p.setName(teacher.getName());
                p.setTheses(teacher.getTheses());
                p.setDepartment(teacher.getDepartment());
                p.setInstitute(teacher.getInstitute());

                p = em.merge(p);
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return null;
        } finally {
            em.close();
        }
        return p;
    }

    @Override
    public List<Pedagog> getTeachers() {
        EntityManager em = emf.createEntityManager();

        List<Pedagog> pl = new ArrayList<>();

        try {
            TypedQuery<Pedagog> p1 = em.createQuery("select p from Pedagog p", Pedagog.class);
            pl = p1.getResultList();
        } catch (Exception e) {
        } finally {
            em.close();
        }
        return pl;
    }

    @Override
    public Pedagog deleteTeacher(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        EntityManager em = emf.createEntityManager();

        Pedagog p;

        em.getTransaction().begin();
        try {
            TypedQuery<Pedagog> p1 = em.createQuery("select p from Pedagog p where p.aisId = :aisId", Pedagog.class);
            p1.setParameter("aisId", id);
            p = p1.getSingleResult();

            for (ZaverecnaPraca zp : p.getTheses()) {
                if (zp.getAuthor() != null) {
                    zp.getAuthor().setThesis(null);
                }
            }
            em.remove(p);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return null;
        } finally {
            em.close();
        }
        return p;
    }

    @Override
    public ZaverecnaPraca makeThesisAssignment(Long supervisor, String registrationNumber, String title, String type, String description) {
        if (supervisor == null) {
            throw new IllegalArgumentException();
        }
        ZaverecnaPraca zp = null;

        EntityManager em = emf.createEntityManager();

        Pedagog p;

        em.getTransaction().begin();
        try {
            TypedQuery<Pedagog> p1 = em.createQuery("select p from Pedagog p where p.aisId = :aisId", Pedagog.class);
            p1.setParameter("aisId", supervisor);
            p = p1.getSingleResult();

            zp = new ZaverecnaPraca();
            zp.setTitle(title);
            zp.setRegistrationNumber(registrationNumber);
            zp.setType(Typ.valueOf(type));
            zp.setDescription(description);
            LocalDate datum = LocalDate.now();
            zp.setPublishedOn(datum);
            zp.setDeadline(datum.plusMonths(3L));
            zp.setDepartment(p.getInstitute());
            zp.setStatus(Status.FREE_TO_TAKE);
            p.getTheses().add(zp);
            zp.setSupervisor(p);
            em.persist(zp);
            em.merge(p);
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            zp = null;
        } finally {
            em.close();
        }

        return zp;
    }

    @Override
    public ZaverecnaPraca assignThesis(Long thesisId, Long studentId) {
        if (thesisId == null || studentId == null) {
            throw new IllegalArgumentException();
        }

        EntityManager em = emf.createEntityManager();

        Student s;
        ZaverecnaPraca zp;

        try {
            TypedQuery<Student> s1 = em.createQuery("select s from Student s where s.aisId = :aisId", Student.class);
            s1.setParameter("aisId", studentId);
            s = s1.getSingleResult();

            TypedQuery<ZaverecnaPraca> zp1 = em.createQuery("select z from ZaverecnaPraca z where z.id = :aisId", ZaverecnaPraca.class);
            zp1.setParameter("aisId", thesisId);
            zp = zp1.getSingleResult();
        } catch (Exception e) {
            em.close();
            return null;
        }

        if (zp.getStatus() != Status.FREE_TO_TAKE || zp.getDeadline().compareTo(LocalDate.now()) <= 0) {
            throw new IllegalStateException();
        }

        if (s.getThesis() != null) {
            throw new IllegalStateException();
        }

        em.getTransaction().begin();
        try {
            zp.setAuthor(s);
            zp.setStatus(Status.IN_PROGRESS);
            s.setThesis(zp);

            zp = em.merge(zp);
            s = em.merge(s);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            zp = null;
        } finally {
            em.close();
        }
        return zp;
    }

    @Override
    public ZaverecnaPraca submitThesis(Long thesisId) {
        if (thesisId == null) {
            throw new IllegalArgumentException();
        }

        EntityManager em = emf.createEntityManager();

        ZaverecnaPraca zp;

        try {
            TypedQuery<ZaverecnaPraca> zp1 = em.createQuery("select z from ZaverecnaPraca z where z.id = :aisId", ZaverecnaPraca.class);
            zp1.setParameter("aisId", thesisId);
            zp = zp1.getSingleResult();
        } catch (Exception e) {
            em.close();
            return null;
        }

        if (zp.getStatus() == Status.SUBMITTED || zp.getStatus() == Status.FREE_TO_TAKE || zp.getDeadline().compareTo(LocalDate.now()) <= 0 || zp.getAuthor() == null) {
            throw new IllegalStateException();
        }

        em.getTransaction().begin();
        try {
            zp.setStatus(Status.SUBMITTED);

            zp = em.merge(zp);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            zp = null;
        } finally {
            em.close();
        }
        return zp;
    }

    @Override
    public ZaverecnaPraca deleteThesis(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        EntityManager em = emf.createEntityManager();

        ZaverecnaPraca zp;

        em.getTransaction().begin();
        try {
           TypedQuery<ZaverecnaPraca> zp1 = em.createQuery("select z from ZaverecnaPraca z where z.id = :aisId", ZaverecnaPraca.class);
            zp1.setParameter("aisId", id);
            zp = zp1.getSingleResult();
            em.remove(zp);
            em.getTransaction().commit();
            em.refresh(zp.getSupervisor());
            if (zp.getAuthor() != null) {
                em.refresh(zp.getAuthor());
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return null;
        } finally {
            em.close();
        }
        return zp;
    }

    @Override
    public List<ZaverecnaPraca> getTheses() {
        EntityManager em = emf.createEntityManager();

        List<ZaverecnaPraca> zpl = new ArrayList<>();

        try {
            TypedQuery<ZaverecnaPraca> zp1 = em.createQuery("select z from ZaverecnaPraca z", ZaverecnaPraca.class);
            zpl = zp1.getResultList();
        } catch (Exception e) {
        } finally {
            em.close();
        }
        return zpl;
    }

    @Override
    public List<ZaverecnaPraca> getThesesByTeacher(Long teacherId) {
        EntityManager em = emf.createEntityManager();

        Pedagog p = new Pedagog();

        try {
            TypedQuery<Pedagog> p1 = em.createQuery("select p from Pedagog p where p.aisId = :aisId", Pedagog.class);
            p1.setParameter("aisId", teacherId);
            p = p1.getSingleResult();
        } catch (Exception e) {
            return new ArrayList<ZaverecnaPraca>();
        } finally {
            em.close();
        }
        return p.getTheses();
    }

    @Override
    public ZaverecnaPraca getThesisByStudent(Long studentId) {
        EntityManager em = emf.createEntityManager();

        Student s = new Student();

        try {
            TypedQuery<Student> s1 = em.createQuery("select s from Student s where s.aisId = :aisId", Student.class);
            s1.setParameter("aisId", studentId);
            s = s1.getSingleResult();
        } catch (Exception e) {
        } finally {
            em.close();
        }
        return s.getThesis();
    }

    @Override
    public ZaverecnaPraca getThesis(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        EntityManager em = emf.createEntityManager();

        ZaverecnaPraca zp = new ZaverecnaPraca();

        try {
            TypedQuery<ZaverecnaPraca> zp1 = em.createQuery("select z from ZaverecnaPraca z where z.id = :aisId", ZaverecnaPraca.class);
            zp1.setParameter("aisId", id);
            zp = zp1.getSingleResult();
        } catch (Exception e) {
            zp = null;
        } finally {
            em.close();
        }
        return zp;
    }

    @Override
    public ZaverecnaPraca updateThesis(ZaverecnaPraca thesis) {
        if (thesis == null || thesis.getId() == null) {
            throw new IllegalArgumentException();
        }
        EntityManager em = emf.createEntityManager();

        ZaverecnaPraca zp = new ZaverecnaPraca();

        em.getTransaction().begin();
        try {
            TypedQuery<ZaverecnaPraca> zp1 = em.createQuery("select z from ZaverecnaPraca z where z.id = :id", ZaverecnaPraca.class);
            zp1.setParameter("id", thesis.getId());
            zp = zp1.getSingleResult();
            zp.setTitle(thesis.getTitle());
            zp.setType(thesis.getType());
            zp.setDescription(thesis.getDescription());
            zp.setPublishedOn(thesis.getPublishedOn());
            zp.setDeadline(thesis.getDeadline());
            zp.setDepartment(thesis.getDepartment());
            zp.setStatus(thesis.getStatus());
            zp.setAuthor(thesis.getAuthor());
            zp.setSupervisor(thesis.getSupervisor());
            em.persist(zp);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            zp = null;
        } finally {
            em.close();
        }
        return zp;
    }

    @Override
    public Page<Student> findStudents(Optional<String> name, Optional<String> year, Pageable pageable) {
        List<Student> s = new ArrayList<>();
        if (name.isPresent() && year.isPresent()) {
            EntityManager em = emf.createEntityManager();
            TypedQuery<Student> s1 = em.createQuery("select s from Student s where s.name = :name AND s.year = :year", Student.class);
            s1.setParameter("name", name.get());
            s1.setParameter("year", year.get());
            s = s1.getResultList();
        } else if (name.isPresent()) {
            EntityManager em = emf.createEntityManager();
            TypedQuery<Student> s1 = em.createQuery("select s from Student s where s.name = :name", Student.class);
            s1.setParameter("name", name.get());
            s = s1.getResultList();
        } else if (year.isPresent()) {
            EntityManager em = emf.createEntityManager();
            TypedQuery<Student> s1 = em.createQuery("select s from Student s where s.year = :year", Student.class);
            s1.setParameter("year", new Integer(year.get()));
            s = s1.getResultList();
        }else{
            EntityManager em = emf.createEntityManager();
            TypedQuery<Student> s1 = em.createQuery("select s from Student s", Student.class);
            s = s1.getResultList();
        }

        int velkostStrany = pageable.getPageSize();
        int aktualnaStrana = pageable.getPageNumber();
        int prvyPrvok = aktualnaStrana * velkostStrany;

        List<Student> result = new ArrayList<>();
        for (int i = prvyPrvok; i < prvyPrvok + velkostStrany; i++) {
            if (i > s.size() - 1) {
                break;
            }
            result.add(s.get(i));
        }

        Pageable1 pa = new Pageable1(aktualnaStrana, velkostStrany);

        Page1 p = new Page1(result, pa, new Long(s.size()));

        return p;
    }

    @Override
    public Page<Pedagog> findTeachers(Optional<String> name, Optional<String> institute, Pageable pageable) {
        List<Pedagog> p = new ArrayList<>();
        if (name.isPresent() && institute.isPresent()) {
            EntityManager em = emf.createEntityManager();
            TypedQuery<Pedagog> p1 = em.createQuery("select p from Pedagog p where p.name = :name AND p.institute = :institute", Pedagog.class);
            p1.setParameter("name", name.get());
            p1.setParameter("institute", institute.get());
            p = p1.getResultList();
        } else if (name.isPresent()) {
            EntityManager em = emf.createEntityManager();
            TypedQuery<Pedagog> p1 = em.createQuery("select p from Pedagog p where p.name = name", Pedagog.class);
            p1.setParameter("name", name.get());
            p = p1.getResultList();
        } else if (institute.isPresent()) {
            EntityManager em = emf.createEntityManager();
            TypedQuery<Pedagog> p1 = em.createQuery("select p from Pedagog p where p.institute = :institute", Pedagog.class);
            p1.setParameter("institute", institute.get());
            p = p1.getResultList();
        }else {
            EntityManager em = emf.createEntityManager();
            TypedQuery<Pedagog> p1 = em.createQuery("select p from Pedagog p", Pedagog.class);
            p = p1.getResultList();
        }

        int velkostStrany = pageable.getPageSize();
        int aktualnaStrana = pageable.getPageNumber();
        int prvyPrvok = aktualnaStrana * velkostStrany;

        List<Pedagog> result = new ArrayList<>();
        for (int i = prvyPrvok; i < prvyPrvok + velkostStrany; i++) {
            if (i > p.size() - 1) {
                break;
            }
            result.add(p.get(i));
        }

        Pageable1 pa = new Pageable1(aktualnaStrana, velkostStrany);

        Page1 pg = new Page1(result, pa, new Long(p.size()));

        return pg;
    }

    @Override
    public Page<ZaverecnaPraca> findTheses(Optional<Long> student, Optional<Long> pedagog, Optional<String> department, Optional<Date> publishedOn, Optional<String> type, Optional<String> status, Pageable pageable) {
        String query = "select z from ZaverecnaPraca z where 1 = 1";
        if (student.isPresent()) {
            query += " AND z.author.aisId = :student";
        }
        if (pedagog.isPresent()) {
            query += " AND z.supervisor.aisId = :pedagog";
        }
        if (department.isPresent()) {
            query += " AND z.department = :department";
        }
        if (publishedOn.isPresent()) {
            query += " AND z.publishedOn = :publishedOn";
        }
        if (type.isPresent()) {
            query += " AND z.type = :type";
        }
        if (status.isPresent()) {
            query += " AND z.status = :status";
        }
        
        EntityManager em = emf.createEntityManager();
        TypedQuery<ZaverecnaPraca> zp1 = em.createQuery(query, ZaverecnaPraca.class);
        if (student.isPresent()) {
            zp1.setParameter("student", student.get());
        }
        if (pedagog.isPresent()) {
            zp1.setParameter("pedagog", pedagog.get());
        }
        if (department.isPresent()) {
            zp1.setParameter("department", department.get());
        }
        if (publishedOn.isPresent()) {
            zp1.setParameter("publishedOn", publishedOn.get().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        if (type.isPresent()) {
            zp1.setParameter("type", Typ.valueOf(type.get().toUpperCase()));
        }
        if (status.isPresent()) {
            zp1.setParameter("status", Status.valueOf(status.get().toUpperCase()));
        }
        List<ZaverecnaPraca> zp = zp1.getResultList();
        
        int velkostStrany = pageable.getPageSize();
        int aktualnaStrana = pageable.getPageNumber();
        int prvyPrvok = aktualnaStrana * velkostStrany;

        List<ZaverecnaPraca> result = new ArrayList<>();
        for (int i = prvyPrvok; i < prvyPrvok + velkostStrany; i++) {
            if (i > zp.size() - 1) {
                break;
            }
            result.add(zp.get(i));
        }

        Pageable1 pa = new Pageable1(aktualnaStrana, velkostStrany);

        Page1 pg = new Page1(result, pa, new Long(zp.size()));

        return pg;
        
    }

}
