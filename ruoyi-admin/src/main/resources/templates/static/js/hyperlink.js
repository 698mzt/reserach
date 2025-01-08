document.addEventListener('DOMContentLoaded', function() {
    // 选取所有添加了link-item类名的元素
    const linkItems = document.querySelectorAll('.link-item');

    linkItems.forEach(function(item) {
        item.addEventListener('click', function() {
            // 这里假设纵向课题、数据中心、下载专区都跳转到不同的页面，你可以根据实际需求修改对应的URL
            if (this.textContent.includes('纵向课题')) {
                window.location.href = 'test-longitudinal-topic.html';
            } else if (this.textContent.includes('数据中心')) {
                window.location.href = 'test-data-center.html';
            } else if (this.textContent.includes('下载专区')) {
                window.location.href = 'test-download-zone.html';
            } else {
                window.location.href = 'login.html';
            }
        });
    });
});