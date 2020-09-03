package com.xframework_uicommon.xadapter;

import androidx.recyclerview.widget.RecyclerView;

import com.xframework_base.xmodel.XBaseModel;
import com.xframework_uicommon.xview.XBaseRefreshFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class XBaseRefreshAdapter<M> extends RecyclerView.Adapter<XBaseRefreshFragment.XBaseRefreshViewHolder<M>> {

    /**
     * 数据同步锁
     */
    private Serializable dataLock = new XBaseModel() {
    };

    /**
     * 数据源集合
     */
    private List<M> dataList = new ArrayList<>();

    /**
     * 重置数据源集合
     * @param dataSources 待重置的数据源集合
     */
    public void setDataSource(List<M> dataSources){
        synchronized (dataLock){
            if(dataSources == null)
                return;
            if(dataList == null)
                return;
            dataList.clear();
            dataList.addAll(dataSources);
            notifyDataSetChanged();
        }
    }

    /**
     * 添加数据源集合
     * @param dataSources 待添加的数据源集合
     */
    public void appendDataSources(List<M> dataSources){
        synchronized (dataLock){
            if(dataSources == null)
                return;
            if(dataList == null)
                return;
            dataList.addAll(dataSources);
            notifyDataSetChanged();
        }
    }

    /**
     * 添加单个数据源集合
     * @param dataSource 待添加的单个数据源
     */
    public void  appendDataSource(M dataSource){
        synchronized (dataLock){
            if(dataSource == null)
                return;
            if(dataList == null)
                return;
            dataList.add(dataSource);
            notifyDataSetChanged();
        }
    }

    /**
     * 清除数据源
     */
    public void clearDataSource(){
        synchronized (dataLock){
            if(dataList == null)
                return;
            dataList.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * 移除指定位置数据源
     * @param position 待移除位置
     */
    public void remove(int position){
        synchronized (dataLock){
            if(position < 0 || position >= dataList.size())
                return;
            if(dataList == null)
                return;
            dataList.remove(position);
            notifyDataSetChanged();
        }
    }

    /**
     * 移除指定对象的数据源
     * @param datasourse 待删除的数据源
     */
    public void remove(M datasourse){
        synchronized (dataLock){
            if(datasourse == null)
                return;
            if(dataList == null)
                return;
            dataList.remove(datasourse);
            notifyDataSetChanged();
        }
    }

    /**
     * 获取数据集合
     * @return 返回数据集合
     */
    public List<M> getDataSources(){
        synchronized (dataLock){
            return dataList;
        }
    }

    /**
     * 获取指定位置的数据
     * @param position 待获取的数据
     * @return 返回指定位置的数据
     */
    public M getDataSource(int position){
        synchronized (dataLock){
            if(position < 0 || position >= dataList.size())
                return null;
            return dataList.get(position);
        }
    }

    @Override
    public void onBindViewHolder(XBaseRefreshFragment.XBaseRefreshViewHolder<M> holder, int position) {
        if(holder == null)
            return;

        synchronized (dataLock){
            if(dataList == null)
                return;
            if(position < 0 || position >= dataList.size())
                return;
        }
        M data = getDataSource(position);
        holder.bindData(data);
    }

    @Override
    public int getItemCount() {
        return getDataSources().size();
    }
}
