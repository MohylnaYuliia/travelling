package travelling.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import travelling.dto.UserRouteDto;
import travelling.entity.UserRouteEntity;
import travelling.mapper.UserRouteEntityToDtoMapper;
import travelling.repository.UserRouteRepository;
import travelling.service.AdminService;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRouteRepository userRouteRepository;

    @Autowired
    protected UserRouteEntityToDtoMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserRouteDto> getAllInformation() {
        List<UserRouteEntity> result = new ArrayList<>();
        userRouteRepository.findAll().forEach(result::add);
        return mapper.map(result);
    }
}
