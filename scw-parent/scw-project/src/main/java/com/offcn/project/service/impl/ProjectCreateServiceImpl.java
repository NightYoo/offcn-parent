package com.offcn.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.offcn.common.enume.ProjectStatusEnume;
import com.offcn.project.enums.ProjectImageTypeEnume;
import com.offcn.project.mapper.*;
import com.offcn.project.pojo.*;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.token.ProjectToken;
import com.offcn.project.vo.req.ProjectRedisStorageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectCreateServiceImpl implements ProjectCreateService {

    @Resource
    private TProjectMapper projectMapper;

    @Resource
    private TProjectImagesMapper imagesMapper;

    @Resource
    private TProjectTagMapper tagMapper;

    @Resource
    private TProjectTypeMapper typeMapper;

    @Resource
    private TReturnMapper returnMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public String initCreateProject(Integer memberId) {
        // 项目的vo对象
        ProjectRedisStorageVo vo = new ProjectRedisStorageVo();
        // 产出一个Project的临时令牌
        String pToken = ProjectToken.PTOKEN + UUID.randomUUID().toString().replace("-","");
        vo.setProjectToken(pToken); // 项目的临时令牌
        vo.setMemberid(memberId); // 项目发起的发起人的ID
        // 把已经存储数据的vo，写入到redis中
        String voString = JSON.toJSONString(vo);
        redisTemplate.opsForValue().set(pToken,voString);
        // 将项目的临时令牌返回
        return pToken;
    }

    @Override
    public void saveProjectInfo(ProjectStatusEnume status, ProjectRedisStorageVo storageVo) {
        TProject tProject = new TProject();
        BeanUtils.copyProperties(storageVo,tProject);
        tProject.setStatus(status.getCode() + "");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tProject.setCreatedate(dateFormat.format(new Date()));
        // 1.项目数据的插入project
        projectMapper.insert(tProject);
        // 获取在project表中插入记录的ID
        Integer projectId = tProject.getId();
        // 2.头图片的插入
        String headerImage = storageVo.getHeaderImage();
        TProjectImages headerImg = new TProjectImages(null, projectId, headerImage, ProjectImageTypeEnume.HEADER.getCode());
        imagesMapper.insert(headerImg);
        // 2.1 详情图的插入
        List<String> detailsImage = storageVo.getDetailsImage();
        if(detailsImage != null && detailsImage.size() > 0) {
            for (String detail : detailsImage) {
                TProjectImages detailImg = new TProjectImages(null, projectId, detail, ProjectImageTypeEnume.DETAILS.getCode());
                imagesMapper.insert(detailImg);
            }
        }
        // 3.标签的插入 tag
        List<Integer> tagids = storageVo.getTagids();
        for(Integer tagId : tagids){
            TProjectTag tProjectTag = new TProjectTag(null, projectId, tagId);
            tagMapper.insert(tProjectTag);
        }
        // 4.类型的插入  type
        List<Integer> typeids = storageVo.getTypeids();
        for(Integer typeId : typeids){
            TProjectType tProjectType = new TProjectType(null, projectId, typeId);
            typeMapper.insert(tProjectType);
        }

        // 5.回报的插入
        List<TReturn> projectReturns = storageVo.getProjectReturns();
        for(TReturn tReturn : projectReturns){
            tReturn.setProjectid(projectId);
            returnMapper.insert(tReturn);
        }
        // 6.删除redis的数据
        //redisTemplate.delete(storageVo.getProjectToken());
    }
}
