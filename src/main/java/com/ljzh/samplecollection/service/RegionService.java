package com.ljzh.samplecollection.service;

import com.ljzh.samplecollection.domain.entity.LayerGroup;
import com.ljzh.samplecollection.domain.entity.Region;
import com.ljzh.samplecollection.domain.vo.RegionVO;
import com.ljzh.samplecollection.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class RegionService {
    @Autowired
    private RegionRepository regionRepository;

    public Region getRegionById(Long id) {
        return regionRepository.findById(id).orElse(null);
    }

    @Transactional
    public Region createRegion(String name, Long parentId, String code) {
        Region region = new Region();
        region.setName(name);
        region.setParentId(parentId);
        region.setCode(code);
        regionRepository.save(region);
        return region;
    }

    public boolean updateRegion(Region region) {
        if (!regionRepository.existsById(region.getId())) {
            return false;
        }
        regionRepository.save(region);
        return true;
    }

    public List<LayerGroup> getLayerGroupsByRegionId(Long regionId) {
        return regionRepository.getOne(regionId).getLayerGroups();
    }

    public boolean deleteRegion(Long id) {
        Region region = regionRepository.findById(id).orElse(null);
        if (region == null) {
            return false;
        }
        if (!region.getLayerGroups().isEmpty()) {
            return false;
        }
        for (Region child : region.getChildren()) {
            child.setParentId(region.getParentId());
            regionRepository.save(child);
        }
        regionRepository.delete(region);
        return true;
    }

    public RegionVO getRegionVO(Long id) {
        Region region = regionRepository.findById(id).orElse(null);
        if (region == null) {
            return null;
        }
        return new RegionVO(region);
    }

    public List<RegionVO> listAllRegions() {
        List<Region> regions = regionRepository.findAll();
        List<RegionVO> regionVOList = new ArrayList<>();
        for (Region region : regions) {
            if (region.getParentId() == null) {
                RegionVO regionVO = new RegionVO(region);
                regionVOList.add(regionVO);
            }
        }
        return regionVOList;
    }

    public RegionVO getRegionVOWithChildren(Long id) {
        Region region = regionRepository.findById(id).orElse(null);
        if (region == null) {
            return null;
        }
        RegionVO regionVO = new RegionVO(region);
        List<RegionVO> childrenVO = new ArrayList<>();
        for (Region child : region.getChildren()) {
            childrenVO.add(getRegionVOWithChildren(child.getId()));
        }
        regionVO.setChildren(childrenVO);
        return regionVO;
    }
}
