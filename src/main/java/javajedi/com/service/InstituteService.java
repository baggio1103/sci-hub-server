package javajedi.com.service;

import javajedi.com.data.InstituteData;

import java.util.List;

public interface InstituteService {

    List<InstituteData> findAllInstitutes();

    InstituteData findInstituteById(String instituteId);

    InstituteData saveInstitute(String country, String name);

    void deleteInstitute(String instituteId);

    InstituteData updateInstitute(String instituteId, String country, String name);

}
