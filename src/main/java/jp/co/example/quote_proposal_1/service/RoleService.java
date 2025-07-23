package jp.co.example.quote_proposal_1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.quote_proposal_1.entity.Role;
import jp.co.example.quote_proposal_1.repository.RoleRepository;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * 全ロールを取得する
     * @return ロールのリスト
     */
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * 指定されたIDのロールを取得する
     * @param id ロールID
     * @return ロール (Optional)
     */
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    /**
     * ロール名でロールを取得する
     * @param name ロール名
     * @return ロール (Optional)
     */
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }
}
