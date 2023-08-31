package kr.co.scheduler.global.entity;


import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "tbl_image")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Img {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(length = 100)
    private String imgPath;

    @Column(length = 100)
    private String imgName;

    @Builder
    public Img(String imgPath, String imgName) {

        this.imgPath = imgPath;
        this.imgName = imgName;
    }
}
