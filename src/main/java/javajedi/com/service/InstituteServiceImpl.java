package javajedi.com.service;

import javajedi.com.data.InstituteData;
import javajedi.com.exception.domain.InstituteNotFoundException;
import javajedi.com.mapper.InstituteMapper;
import javajedi.com.model.Institute;
import javajedi.com.repository.InstituteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static javajedi.com.constant.InstituteConstant.NO_INSTITUTE_FOUND_BY_INSTITUTE_ID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class InstituteServiceImpl implements InstituteService {

    private final InstituteRepository instituteRepository;

    private final InstituteMapper instituteMapper;

    @Override
    public List<InstituteData> findAllInstitutes() {
        return instituteMapper.mapInstitutes(instituteRepository.findAll());
    }

    @Override
    public InstituteData findInstituteById(String instituteId) {
        Institute institute = instituteRepository.findInstituteByInstituteId(instituteId)
                .orElseThrow(() -> new InstituteNotFoundException(NO_INSTITUTE_FOUND_BY_INSTITUTE_ID + instituteId));
        return instituteMapper.toInstituteData(institute);
    }

    @Override
    public InstituteData saveInstitute(String country, String name) {
        Institute institute = new Institute();
        institute.setInstituteId(generateId());
        institute.setCountry(country);
        institute.setName(name);
        return instituteMapper.toInstituteData(instituteRepository.save(institute));
    }

    @Override
    public void deleteInstitute(String instituteId) {
        Institute institute = instituteRepository.findInstituteByInstituteId(instituteId)
                .orElseThrow(() -> new InstituteNotFoundException(NO_INSTITUTE_FOUND_BY_INSTITUTE_ID + instituteId));
        institute.getUsers().forEach(user -> user.setInstitute(null));
        instituteRepository.delete(institute);
    }

    @Override
    public InstituteData updateInstitute(String instituteId, String country, String name) {
        Institute institute = instituteRepository.findInstituteByInstituteId(instituteId)
                .orElseThrow(() -> new InstituteNotFoundException(NO_INSTITUTE_FOUND_BY_INSTITUTE_ID + instituteId));
        institute.setName(name);
        institute.setCountry(country);
        instituteRepository.save(institute);
        return instituteMapper.toInstituteData(institute);
    }

    private String generateId() {
        return randomAlphanumeric(10) + instituteRepository.count();
    }

}
