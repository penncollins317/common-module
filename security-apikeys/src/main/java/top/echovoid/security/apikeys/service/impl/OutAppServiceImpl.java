package top.echovoid.security.apikeys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.echovoid.common.exceptions.ServiceException;
import top.echovoid.common.utils.DeepBeanUtil;
import top.echovoid.security.apikeys.dto.request.CreateOutAppRequestDTO;
import top.echovoid.security.apikeys.dto.response.AppInfoDTO;
import top.echovoid.security.apikeys.dto.response.OutAppDTO;
import top.echovoid.security.apikeys.entity.OutApp;
import top.echovoid.security.apikeys.enums.OutAppStatus;
import top.echovoid.security.apikeys.mapper.OutAppMapper;
import top.echovoid.security.apikeys.service.OutAppService;
import top.echovoid.security.apikeys.utils.SecretKeyUtil;

import java.util.List;

/**
 * @author Penn Collins
 * @since 2025/2/15
 */
@Service
@AllArgsConstructor
public class OutAppServiceImpl implements OutAppService {
    private final OutAppMapper outAppMapper;

    @Override
    @Transactional
    public Long createApp(CreateOutAppRequestDTO dto) {
        if (this.outAppMapper.exists(new QueryWrapper<OutApp>().eq("name", dto.getName()))) {
            throw new ServiceException("应用名称已存在");
        }

        OutApp outApp = DeepBeanUtil.copyProperties(dto, OutApp::new);
        this.outAppMapper.insert(outApp);
        return outApp.getId();
    }

    @Override
    public AppInfoDTO getAppInfo(Long appId, Long userId) {
        QueryWrapper<OutApp> queryWrapper = new QueryWrapper<OutApp>().eq("id", appId).eq("user_id", userId);
        OutApp outApp = this.outAppMapper.selectOne(queryWrapper);
        return DeepBeanUtil.copyProperties(outApp, AppInfoDTO::new);
    }

    @Override
    @Transactional
    public String resetSecretKey(Long appId, Long userId) {
        AppInfoDTO appInfo = this.getAppInfo(appId, userId);
        if (appInfo == null) {
            throw new ServiceException("应用不存在");
        }
        OutApp outApp = new OutApp();
        outApp.setId(appInfo.getId());
        outApp.setSecretKey(SecretKeyUtil.generateSecretKey(64));
        this.outAppMapper.updateById(outApp);
        return outApp.getSecretKey();
    }

    @Override
    @Transactional
    public boolean updateAesKey(Long appId, Long userId, String aesKey) {
        AppInfoDTO appInfo = this.getAppInfo(appId, userId);
        if (appInfo == null) {
            throw new ServiceException("应用不存在");
        }
        OutApp outApp = new OutApp();
        outApp.setId(appInfo.getId());
        outApp.setAesKey(aesKey);
        return this.outAppMapper.updateById(outApp) > 0;
    }

    @Override
    public List<OutAppDTO> listApp(Long userId) {
        QueryWrapper<OutApp> queryWrapper = new QueryWrapper<OutApp>().eq("user_id", userId);
        List<OutApp> outApps = this.outAppMapper.selectList(queryWrapper);
        return DeepBeanUtil.copyProperties(outApps, OutAppDTO::new);
    }

    @Override
    @Transactional
    public boolean disableApp(Long appId, Long userId) {
        AppInfoDTO appInfo = this.getAppInfo(appId, userId);
        if (appInfo == null) {
            throw new ServiceException("应用不存在");
        }
        if (appInfo.getStatus() != OutAppStatus.ACTIVE) {
            return false;
        }
        OutApp outApp = new OutApp();
        outApp.setId(appInfo.getId());
        outApp.setStatus(OutAppStatus.DISABLE);
        return this.outAppMapper.updateById(outApp) > 0;
    }
}
