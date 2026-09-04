package org.project.cloud.file.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "path", nullable = false, length = 60)
    private String path;

    @Column(name = "size", nullable = false, length = 30)
    private Long size;

    @Column(name = "file_type", nullable = false, length = 20)
    private FileType fileType;

    @Column(name = "user_id", nullable = false, length = 20)
    private Long userId;

}
