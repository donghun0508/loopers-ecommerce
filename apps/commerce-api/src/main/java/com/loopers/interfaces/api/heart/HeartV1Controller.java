package com.loopers.interfaces.api.heart;

import com.loopers.application.heart.CriteriaCommand;
import com.loopers.application.heart.CriteriaCommand.LikeCriteria;
import com.loopers.application.heart.HeartFacade;
import com.loopers.application.heart.HeartQueryFacade;
import com.loopers.application.heart.HeartResults.HeartResult;
import com.loopers.domain.heart.TargetType;
import com.loopers.interfaces.api.ApiHeaders;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.PaginationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/like")
public class HeartV1Controller implements HeartV1ApiSpec {

    private final HeartFacade heartFacade;
    private final HeartQueryFacade heartQueryFacade;

    @Override
    @PostMapping("/products/{productId}")
    public ApiResponse<?> addHeart(
            @PathVariable Long productId,
            @RequestHeader(value = ApiHeaders.USER_ID, required = true) String userId
    ) {
        LikeCriteria criteria = LikeCriteria.of(userId, productId, TargetType.PRODUCT);
        heartFacade.heart(criteria);
        return ApiResponse.success();
    }

    @Override
    @DeleteMapping("/products/{productId}")
    public ApiResponse<?> removeHeart(
            @PathVariable Long productId,
            @RequestHeader(value = ApiHeaders.USER_ID, required = true) String userId
    ) {
        CriteriaCommand.UnlikeCriteria criteria = CriteriaCommand.UnlikeCriteria.of(userId, productId, TargetType.PRODUCT);
        heartFacade.unHeart(criteria);
        return ApiResponse.success();
    }

    @Override
    @GetMapping("/products")
    public ApiResponse<Page<HeartV1Dto.Response>> getHeartList(
            @ModelAttribute PaginationRequest paginationRequest,
            @RequestHeader(value = ApiHeaders.USER_ID, required = true) String userId
    ) {
        Page<HeartResult> heartResults = heartQueryFacade.getHeartList(userId, paginationRequest.toPageable());
        return ApiResponse.success(heartResults.map(HeartV1Dto.Response::from));
    }
}
