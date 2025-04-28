package vn.hoidanit.jobhunter.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResultPaginationDTO<T> {

    private Meta meta;
    private T result;

    public ResultPaginationDTO() {}
}
