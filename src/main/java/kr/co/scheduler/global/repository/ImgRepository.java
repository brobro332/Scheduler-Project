package kr.co.scheduler.global.repository;

import kr.co.scheduler.global.entity.Img;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImgRepository extends JpaRepository<Img, Long> {

    Img findByImgName(String uploadFileName);
}
