package models;

import javax.validation.constraints.*;

public class ModelWithBeanValidations {
  @NotNull
  protected Long id;

  @Min(13) @Max(99)
  protected Integer age;

  @Size(min=6, max=20)
  protected String password;

  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }

  public Integer getAge() {
    return age;
  }
  public void setAge(Integer age) {
    this.age = age;
  }

  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
}