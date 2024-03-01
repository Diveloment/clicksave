package com.altinntech.clicksave.examples.entity;

import com.altinntech.clicksave.annotations.*;
import com.altinntech.clicksave.core.CSUtils;
import com.altinntech.clicksave.enums.EnumType;
import com.altinntech.clicksave.enums.FieldType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@ClickHouseEntity // you should use this annotation for persistence entity
@Batching(batchSize = 10) // add batch for saving
public class Person {

    // entity class must have a no arguments constructor
    public Person() {
    }

    @Column(value = FieldType.LONG, id = true) // it is recommended to make the id field a UUID type
    Long id;
    @Column(FieldType.STRING)
    String name;
    @Column(FieldType.STRING)
    String lastName;
    @Column(FieldType.INT)
    Integer age; // not supported primitive types
    @Column(FieldType.STRING)
    String address;
    @EnumColumn(EnumType.STRING) // for enum use @EnumColumn annotation
    Gender gender;
    @EnumColumn(EnumType.BY_ID) // you can persist the enum by id value (enum must implements the EnumId interface!)
    Job job;
    @Embedded
    EmployeeInfo employeeInfo;
    @Lob
    List<CompanyMetadata> companyMetadata;
    @Lob
    CompanyMetadata companyMetadataSingle;
    String noSaveField; // this field will not be saved

    public Person(Long id, String name, String lastName, Integer age, String address, Gender gender, Job job, String noSaveField) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.address = address;
        this.gender = gender;
        this.job = job;
        this.noSaveField = noSaveField;
        this.employeeInfo = buildMockEmployeeInfo();
        this.companyMetadata = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CompanyMetadata metadata = buildMockCompanyMetadata();
            this.companyMetadata.add(metadata);
        }
        this.companyMetadataSingle = buildMockCompanyMetadata();
    }

    public static Person buildMockPerson() {
        Person person = new Person();
        person.name = CSUtils.generateRandomString(5);
        person.lastName = CSUtils.generateRandomString(10);
        person.age = CSUtils.generateRandomNumber(1, 99);
        person.address = CSUtils.generateRandomString(15);
        person.gender = CSUtils.getRandomEnum(Gender.class);
        person.job = CSUtils.getRandomEnum(Job.class);
        person.employeeInfo = buildMockEmployeeInfo();
        person.companyMetadata = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CompanyMetadata metadata = buildMockCompanyMetadata();
            person.companyMetadata.add(metadata);
        }
        person.companyMetadataSingle = buildMockCompanyMetadata();
        return person;
    }

    public static EmployeeInfo buildMockEmployeeInfo() {
        EmployeeInfo employeeInfo = new EmployeeInfo();
        employeeInfo.description = CSUtils.generateRandomString(15);
        employeeInfo.experience = (float) CSUtils.generateRandomNumber(1, 99);
        employeeInfo.setWorkInfo(buildMockWorkInfo());
        return employeeInfo;
    }

    public static WorkInfo buildMockWorkInfo() {
        WorkInfo workInfo = new WorkInfo();
        workInfo.setWorkName(CSUtils.generateRandomString(5));
        workInfo.setGrade(CSUtils.generateRandomNumber(1, 99));
        return workInfo;
    }

    public static CompanyMetadata buildMockCompanyMetadata() {
        CompanyMetadata companyMetadata = new CompanyMetadata();
        companyMetadata.setCompanyName(CSUtils.generateRandomString(7));
        companyMetadata.setCompanyIndex((long) CSUtils.generateRandomNumber(10000, 99999));
        return companyMetadata;
    }
}
