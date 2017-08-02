package com.justynsoft.simplerecon.core.worker;

import javax.persistence.*;

@Entity
@Table(name = "recon_workers")
public class ReconWorkerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @Column(name = "recon_service_id")
    private long reconServiceId;
    @Column(name = "class_name")
    private String className;
    @Column (name = "target_object_class_name")
    private String targetObjectClassName;
    @Column (name = "bean_name")
    private String beanName;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getReconServiceId() {
        return reconServiceId;
    }

    public void setReconServiceId(long reconServiceId) {
        this.reconServiceId = reconServiceId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getTargetObjectClassName() {
        return targetObjectClassName;
    }

    public void setTargetObjectClassName(String targetObjectClassName) {
        this.targetObjectClassName = targetObjectClassName;
    }
}
