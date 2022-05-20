package travelling.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import travelling.entity.UserRouteEntity;
import travelling.repository.UserRouteRepository;
import travelling.service.AdminService;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRouteRepository userRouteRepository;

    @Override
    public List<UserRouteEntity> getAllInformation() {
        List<UserRouteEntity> result = new ArrayList<>();
        userRouteRepository.findAll().forEach(result::add);
        return result;
    }
}
