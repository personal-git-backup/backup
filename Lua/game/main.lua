screen = {
    W = 320,
    H = 480
}

aviao_14bis = {
    src = 'images/14bis.png',
    srcExplosion = 'images/explosao_nave.png',
    x = screen.W - 64,
    y = screen.H - 63,
    w = 64,
    h = 63
}

MAX_METEORS = 12
SHOOT_SPEED = 5

MAX_METEORS_SHOOTED = 10
METEORS_SHOOTED = 0

meteors = {}
shoots = {}

function meteor(sx, sy)
    if #meteors < MAX_METEORS then
        table.insert(meteors, {
            x = sx,
            y = sy,
            verticalWeight = math.random(3),
            horizontalWeight = math.random(-1, 1),
            w = 64,
            h = 63
        })
    end
end

function shoot()
    table.insert(shoots , {
        x = aviao_14bis.x - aviao_14bis.w/4,
        y = aviao_14bis.y,
        w = 16,
        h = 16
    })
    table.insert(shoots , {
        x = aviao_14bis.x + aviao_14bis.w/4,
        y = aviao_14bis.y,
        w = 16,
        h = 16
    })
    shoot_audio:play()
end

function check_win()
    if METEORS_SHOOTED < MAX_METEORS_SHOOTED then return end
    
    ambient_audio:stop()
    win_audio:play()
    GAME_OVER = true
    WON = true
end

function check_colision(item1, item2)
    return item2.x < item1.x + item1.w and
        item2.y < item1.y + item1.h and
        item1.x < item2.x + item2.w and
        item1.y < item2.y + item2.h
end

function update_meteors()
    for i,met in pairs(meteors) do
        if met.y < screen.H and met.x + met.w > 0 and met.x < screen.W then
            met.y = met.y + met.verticalWeight
            met.x = met.x + met.horizontalWeight

            if check_colision(met, aviao_14bis) then
                ambient_audio:stop()
                destruicao_audio:play()
                gameover_audio:play()

                aviao_14bis.cur_img = aviao_14bis.explosion_img
                GAME_OVER = true
            end
        else
            table.remove(meteors, i)
        end
    end
end

function update_shoots()
    for i,shoot in pairs(shoots) do
        if shoot.y+shoot.w > 0 then
            shoot.y = shoot.y - SHOOT_SPEED

            for j,met in pairs(meteors) do
                if check_colision(shoot, met) then
                    METEORS_SHOOTED = METEORS_SHOOTED+1
                    table.remove(shoots, i)
                    table.remove(meteors, j)
                end
            end

        else
            table.remove(shoots, i)
        end
    end
end

function move_14bis()
    if love.keyboard.isDown('w') and aviao_14bis.y > 0 then
        aviao_14bis.y = aviao_14bis.y - 2
    end
    if love.keyboard.isDown('a') and aviao_14bis.x > 0 then
        aviao_14bis.x = aviao_14bis.x - 2
    end
    if love.keyboard.isDown('s') and aviao_14bis.y + aviao_14bis.h < screen.H then
        aviao_14bis.y = aviao_14bis.y + 2
    end
    if love.keyboard.isDown('d') and aviao_14bis.x + aviao_14bis.w < screen.W then
        aviao_14bis.x = aviao_14bis.x + 2
    end
end

-- Load some default values for our rectangle.
function love.load()
    love.window.setMode(screen.W, screen.H)

    background = love.graphics.newImage('images/background.png')
    aviao_14bis.img = love.graphics.newImage(aviao_14bis.src)
    aviao_14bis.explosion_img = love.graphics.newImage(aviao_14bis.srcExplosion)
    aviao_14bis.cur_img = aviao_14bis.img
    
    meteor_img = love.graphics.newImage('images/meteoro.png')
    shoot_img = love.graphics.newImage('images/tiro.png')
    gameover_img = love.graphics.newImage('images/gameover.png')
    youwin_img = love.graphics.newImage('images/vencedor.png')

    ambient_audio = love.audio.newSource('audios/ambiente.wav', 'static')
    ambient_audio:setLooping(true)
    ambient_audio:play()

    destruicao_audio = love.audio.newSource('audios/destruicao.wav', 'static')
    gameover_audio = love.audio.newSource('audios/game_over.wav', 'static')
    shoot_audio = love.audio.newSource('audios/disparo.wav', 'static')
    win_audio = love.audio.newSource('audios/winner.wav', 'static')
end

-- Increase the size of the rectangle every frame.
function love.update(dt)
    if not GAME_OVER then
        if love.keyboard.isDown('w', 'a', 's', 'd') then
            move_14bis()
        end
        
        if love.keyboard.isDown('space') and false then -- remove 'false' check to enable pressing space shoot
            shoot()
        end
        
        meteor(math.random(screen.W), -64)
        update_meteors()
        update_shoots()
        check_win()
    end
end

function love.keypressed(key)
    if key == 'escape' then
        love.event.quit()
        return
    end

    if GAME_OVER then return end

    if key == 'space' then
        shoot()
        return
    end
end

-- Draw a coloured rectangle.
function love.draw()
    love.graphics.draw(background, 0, 0)
    
    for i,met in pairs(meteors) do
        love.graphics.draw(meteor_img, met.x, met.y)
    end

    for i,s in pairs(shoots) do
        love.graphics.draw(shoot_img, s.x, s.y)
    end
    
    love.graphics.draw(aviao_14bis.cur_img, aviao_14bis.x, aviao_14bis.y)
    love.graphics.print("Meteoros destruídos: " .. METEORS_SHOOTED, 0, 0)
    
    if GAME_OVER then
        if WON then
            love.graphics.draw(youwin_img, screen.W/2 - youwin_img:getWidth()/2, screen.H/2 - youwin_img:getHeight()/2)
        else
            love.graphics.draw(gameover_img, screen.W/2 - gameover_img:getWidth()/2, screen.H/2 - gameover_img:getHeight()/2)
        end

    end
end
