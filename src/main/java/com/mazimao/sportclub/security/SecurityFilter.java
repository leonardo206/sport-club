package com.mazimao.sportclub.security;

import com.mazimao.sportclub.service.dto.OrganizationCriteria;
import java.util.List;

public interface SecurityFilter<T> {
    T setSecurityCriteria(T criteria, List<String> authorities);
}
