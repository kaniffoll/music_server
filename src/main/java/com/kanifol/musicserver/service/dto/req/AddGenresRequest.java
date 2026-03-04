package com.kanifol.musicserver.service.dto.req;

import java.util.Set;

public record AddGenresRequest(
        Set<String> genreNames
) {}