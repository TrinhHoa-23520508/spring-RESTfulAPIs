package vn.hoidanit.jobhunter.service;

import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.util.error.NotFoundException;
import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Company not found with id: " + id));
    }

    public void deleteCompanyById(Long id) {
        companyRepository.deleteById(id);
    }

    public Company updateCompany(Company company) {
        Company oldCompany = companyRepository.findById(company.getId())
                .orElseThrow(() -> new NotFoundException("Company not found with id: " + company.getId()));

        if (isValid(company.getName())) {
            oldCompany.setName(company.getName());
        }
        if (isValid(company.getAddress())) {
            oldCompany.setAddress(company.getAddress());
        }
        if (isValid(company.getDescription())) {
            oldCompany.setDescription(company.getDescription());
        }
        if (isValid(company.getLogo())) {
            oldCompany.setLogo(company.getLogo());
        }
        return companyRepository.save(oldCompany);
    }

    private boolean isValid(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
