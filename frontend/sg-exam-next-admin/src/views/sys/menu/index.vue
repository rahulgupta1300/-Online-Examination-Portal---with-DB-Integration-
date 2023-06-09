<template>
  <div>
    <BasicTable @register="registerTable" @fetch-success="onFetchSuccess">
      <template #toolbar>
        <a-button v-if="hasPermission(['sys:menu:add'])" type="primary" @click="handleCreate"> 新增菜单 </a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'clarity:note-edit-line',
              onClick: handleEdit.bind(null, record),
              auth: 'sys:menu:edit'
            },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
              auth: 'sys:menu:del',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDelete.bind(null, record),
              },
            },
          ]"
        />
      </template>
    </BasicTable>
    <MenuDrawer @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
import { defineComponent, nextTick } from 'vue';

import { BasicTable, useTable, TableAction } from '/@/components/Table';
import { getMenuList, deleteMenu } from '/@/api/sys/menu';

import { useDrawer } from '/@/components/Drawer';
import MenuDrawer from './MenuDrawer.vue';

import { columns, searchFormSchema } from './menu.data';
import { usePermission } from '/@/hooks/web/usePermission';
import {useMessage} from "/@/hooks/web/useMessage";

export default defineComponent({
  name: 'MenuManagement',
  components: { BasicTable, MenuDrawer, TableAction },
  setup() {
    const { hasPermission } = usePermission();
    const { createMessage } = useMessage();
    const [registerDrawer, { openDrawer }] = useDrawer();
    const [registerTable, { reload, expandAll }] = useTable({
      title: '菜单列表',
      api: getMenuList,
      columns,
      formConfig: {
        labelWidth: 120,
        schemas: searchFormSchema,
      },
      isTreeTable: true,
      pagination: false,
      striped: false,
      useSearchForm: true,
      showTableSetting: true,
      bordered: true,
      showIndexColumn: false,
      canResize: false,
      actionColumn: {
        width: 80,
        title: '操作',
        dataIndex: 'action',
        slots: { customRender: 'action' },
        fixed: undefined,
      },
    });

    function handleCreate() {
      openDrawer(true, {
        isUpdate: false,
      });
    }

    function handleEdit(record: Recordable) {
      openDrawer(true, {
        record,
        isUpdate: true,
      });
    }

    async function handleDelete(record: Recordable) {
      await deleteMenu(record.id);
      createMessage.success('操作成功');
      await reload();
    }

    function handleSuccess() {
      createMessage.success('操作成功');
      reload();
    }

    function onFetchSuccess() {
      nextTick(expandAll);
    }

    return {
      hasPermission,
      registerTable,
      registerDrawer,
      handleCreate,
      handleEdit,
      handleDelete,
      handleSuccess,
      onFetchSuccess,
    };
  },
});
</script>
