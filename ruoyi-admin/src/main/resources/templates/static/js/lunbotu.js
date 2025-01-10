function toggleSearch() {
    const searchSection = document.querySelector('.search-section');
    const input = searchSection.querySelector('input[type="text"]');
    const quickLinks = document.getElementById('quickLinks');
    searchSection.classList.toggle('active');
    quickLinks.classList.toggle('active');
    if (searchSection.classList.contains('active')) {
        input.focus(); // 点击后聚焦到搜索框
    } else {
        // 可以在这里添加代码来关闭搜索框时清空内容或执行其他操作
    }
}


window.onload=function (){
    // let imglist = document.querySelectorAll('.imglist>a')
    // let leftBtn = document.querySelector('.leftBtn')
    // let rightBtn = document.querySelector('.rightBtn')
    const imglist = document.querySelectorAll('.imglist>a');
    const leftBtn = document.querySelector('.leftBtn');
    const rightBtn = document.querySelector('.rightBtn');
    let btnlist = document.querySelectorAll('.btnlist>span')
    let index = 0;
    let time ;
    // 页面加载时先明确隐藏左右切换按钮
    leftBtn.style.display = 'none';
    rightBtn.style.display = 'none';
    autoplay();
    btnClick();

    // 鼠标悬停在图片上时显示按钮
    imglist.forEach((img) => {
        img.addEventListener('mouseenter', () => {
            leftBtn.style.display = 'block';
            rightBtn.style.display = 'block';
        });

        // 鼠标移出图片时隐藏按钮
        img.addEventListener('mouseleave', () => {
            leftBtn.style.display = 'none';
            rightBtn.style.display = 'none';
        });
    });

    function autoplay(){
        time = setInterval(()=>{
            // index++;
            index == imglist.length-1?index=0:index++;//循环播放，从一张到最后一张再从头来
            showImage();
        },3000)//3000ms等于3s;
    }
    leftBtn.onclick = function (){
        clearInterval(time);
        index==0?index=imglist.length-1:index--;//循环播放，从最后一张到第一张
        showImage();
        autoplay();
    }
    rightBtn.onclick = function (){
        clearInterval(time);
        index == imglist.length-1?index=0:index++;//循环播放，从一张到最后一张再从头来
        showImage();
        autoplay();
    }

    //公共代码
    function showImage(){
        for (let i=0;i<imglist.length;i++){
            imglist[i].classList.remove('show');
            btnlist[i].classList.remove('che');
        }
        imglist[index].classList.add('show')
        btnlist[index].classList.add('che')
    }
    function btnClick(){
        for (let i=0;i<btnlist.length;i++) {
            btnlist[i].onclick=function (){
                clearInterval(time);
                index=i;
                showImage();
                autoplay();
            }
        }
    }
}