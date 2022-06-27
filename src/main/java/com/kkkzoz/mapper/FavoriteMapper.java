package com.kkkzoz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kkkzoz.domain.entity.Favorite;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component("favoriteMapper")
public interface FavoriteMapper  extends BaseMapper<Favorite> {
}
