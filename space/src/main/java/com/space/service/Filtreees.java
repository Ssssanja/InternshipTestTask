package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.Optional;

public class Filtreees {
    public static Specification<Ship> stringPFilter (String par, Optional<String> value) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (!value.isPresent())
                    return criteriaBuilder.like(root.get(par), "%");
                else
                    return criteriaBuilder.like(root.get(par), "%" + value.get() + "%" );
            }
        };
    }

    public static Specification<Ship> crewFilter (String par, Optional<Integer> minValue, Optional<Integer> maxValue) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Integer localMinValue = Integer.MIN_VALUE;
                Integer localMaxValue = Integer.MAX_VALUE;
                if (minValue.isPresent())
                    localMinValue = minValue.get();
                if (maxValue.isPresent())
                    localMaxValue = maxValue.get();
                return criteriaBuilder.between(root.get(par), localMinValue, localMaxValue);
            }
        };
    }

    public static Specification<Ship> speedFilter (String par, Optional<Double> minValue, Optional<Double> maxValue) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Double localMinValue = Double.MIN_VALUE;
                Double localMaxValue = Double.MAX_VALUE;
                if (minValue.isPresent())
                    localMinValue = minValue.get();
                if (maxValue.isPresent())
                    localMaxValue = maxValue.get();
                return criteriaBuilder.between(root.get(par), localMinValue, localMaxValue);
            }
        };
    }

    public static Specification<Ship> ratingFilter (String par, Optional<Double> minValue, Optional<Double> maxValue) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Double localMinValue = Double.MIN_VALUE;
                Double localMaxValue = Double.MAX_VALUE;
                if (minValue.isPresent())
                    localMinValue = minValue.get();
                if (maxValue.isPresent())
                    localMaxValue = maxValue.get();
                return criteriaBuilder.between(root.get(par), localMinValue, localMaxValue);
            }
        };
    }

    public static Specification<Ship> booleanFilter (String par, Optional<Boolean> value) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (value.isPresent()) {
                    if (value.get())
                        return criteriaBuilder.isTrue(root.get(par));
                    else
                        return criteriaBuilder.isFalse(root.get(par));
                } else
                    return criteriaBuilder.isNotNull(root.get(par));

            }
        };
    }

    public static Specification<Ship> dateFilter (String par, Optional<Long> minValue, Optional<Long> maxValue) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                long localMinValue = ShipServImpl.EARLEST_DATE;
                long localMaxValue = ShipServImpl.LASTEST_DATE;
                if (minValue.isPresent())
                    localMinValue = minValue.get();
                if (maxValue.isPresent())
                    localMaxValue = maxValue.get();
                return criteriaBuilder.between(root.get(par), new Date(localMinValue), new Date(localMaxValue));
            }
        };
    }


    public static Specification<Ship> typeFilter (String par, Optional<ShipType> value) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (!value.isPresent())
                    return criteriaBuilder.isNotNull(root.get(par));
                else
                    return criteriaBuilder.equal(root.get(par), value.get());
            }
        };
    }
}